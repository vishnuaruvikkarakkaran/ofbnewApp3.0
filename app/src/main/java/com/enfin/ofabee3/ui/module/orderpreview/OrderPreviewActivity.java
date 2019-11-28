package com.enfin.ofabee3.ui.module.orderpreview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.applycoupon.response.ApplyCouponResponse;
import com.enfin.ofabee3.data.remote.model.createorder.response.CreateOrderResponse;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.ui.module.Payment.PaymentActivity;
import com.enfin.ofabee3.ui.module.coursedetail.TabsHeaderActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.MultiTextWatcher;
import com.enfin.ofabee3.utils.OBLogger;
import com.google.android.material.snackbar.Snackbar;

import hk.ids.gws.android.sclick.SClick;

public class OrderPreviewActivity extends BaseActivity implements OrderPreviewContract.View {

    private TextView haveCouponTV, couponRemoveTV, invalidCouponTV, couponDiscountPriceTV;
    private Button applyButton;
    private TextView courseName, totalAmountTV, subtotalAmountTV, taxAmountTV;
    private LinearLayout applyCouponLayout, appliedCouponDiscountLL;
    private EditText etCouponCode;
    private Button checkoutBtn;
    private AppCompatImageView toolbarBack;
    private OrderPreviewContract.Presenter mPresenter;
    private Dialog progressDialog;
    private String imagePath, name, orderID, courseID, itemType;
    private ImageView courseMainImage;
    private RatingBar courseRatingBar;
    private int amount;
    private LinearLayout taxLayout;
    private TextView discountTag;
    private double subTotal, grandTotal, taxTotal, taxrate;
    private int taxType;
    private boolean isRatingEnabled;
    private float ratingValue;
    private double subTotalBeforeCoupon, taxBeforeCoupon, totalBeforeCoupon;
    private String validCouponCode = "DISCOUNT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        haveCouponTV = findViewById(R.id.have_coupon_tv);
        couponRemoveTV = findViewById(R.id.coupon_remove_tv);
        courseName = findViewById(R.id.tv_course_name);
        applyButton = findViewById(R.id.apply_button);
        applyCouponLayout = findViewById(R.id.apply_coupon_ll);
        appliedCouponDiscountLL = findViewById(R.id.coupon_applied_discount_ll);
        etCouponCode = findViewById(R.id.et_coupon_code);
        taxLayout = findViewById(R.id.tax_ll);
        couponDiscountPriceTV = findViewById(R.id.coupon_discount_price_tv);
        progressDialog = AppUtils.showProgressDialog(this);
        invalidCouponTV = findViewById(R.id.invalid_coupon_tv);
        totalAmountTV = findViewById(R.id.total_amount_tv);
        subtotalAmountTV = findViewById(R.id.subtotal_amount_tv);
        taxAmountTV = findViewById(R.id.tax_amount_tv);
        courseMainImage = findViewById(R.id.course_header_image);
        courseRatingBar = findViewById(R.id.course_rating_bar);
        checkoutBtn = findViewById(R.id.btn_checkout);
        toolbarBack = findViewById(R.id.toolbar_back);
        discountTag = findViewById(R.id.discount_tag_tv);
        toolbarBack.setOnClickListener(v -> finish());
        imagePath = getIntent().getStringExtra("image");
        name = getIntent().getStringExtra("name");
        orderID = getIntent().getStringExtra("orderID");
        courseID = getIntent().getStringExtra("courseID");
        itemType = getIntent().getStringExtra("type");
        taxrate = Double.parseDouble(getIntent().getStringExtra("taxRate"));
        taxType = Integer.parseInt(getIntent().getStringExtra("taxType"));
        amount = Integer.parseInt(getIntent().getStringExtra("amount").split(" ")[1]);
        isRatingEnabled = getIntent().getBooleanExtra("ratingStatus", false);
        ratingValue = getIntent().getFloatExtra("rating", 0f);
        OBLogger.e("values " + amount + " " + taxrate + " " + taxType);
        etCouponCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        if (taxType == 0) {
            subtotalAmountTV.setText(" ₹ " + String.format("%.2f", (double) amount));
            taxAmountTV.setText(" ₹ 0");
            totalAmountTV.setText(" ₹ " + String.format("%.2f", (double) amount));
            taxTotal = 0;
            subTotal = amount;
            grandTotal = amount;
        } else if (taxType == 1) {
            subtotalAmountTV.setText(" ₹ " + String.format("%.2f", (double) amount));
            taxAmountTV.setText(" ₹ 0");
            totalAmountTV.setText(" ₹ " + String.format("%.2f", (double) amount));
            subTotal = amount;
            taxTotal = 0;
            grandTotal = amount;
        }

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(this)
                .load(imagePath)
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(courseMainImage);
        courseName.setText(Html.fromHtml(name));
        mPresenter = new OrderPreviewPresenter(this, this);
        if (isRatingEnabled) {
            courseRatingBar.setVisibility(View.VISIBLE);
            courseRatingBar.setRating(ratingValue);
        } else
            courseRatingBar.setVisibility(View.INVISIBLE);

        if (taxType == 1)
            calculateExclusiveTax();
        else if (taxType == 0)
            calculateInclusiveTax();

        haveCouponTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                haveCouponTV.setVisibility(View.INVISIBLE);
                applyCouponLayout.setVisibility(View.VISIBLE);
                etCouponCode.setText("");

            }
        });

        applyButton.setOnClickListener(view -> {
            if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
            if (etCouponCode.getText().toString().length() < 3) {
                etCouponCode.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.round_edge_layout_coupon_apply_error));
                applyButton.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.round_edge_apply_button_error_bg));
            } else {
                hideKeyboard(OrderPreviewActivity.this);
                //AppUtils.showDiscountDialog(OrderPreviewActivity.this, getLayoutInflater(), "Discount");
                mPresenter.applyCouponCode(this, etCouponCode.getText().toString());
                discountTag.setText(etCouponCode.getText().toString());
            }
        });

        couponRemoveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                haveCouponTV.setVisibility(View.VISIBLE);
                applyCouponLayout.setVisibility(View.GONE);
                appliedCouponDiscountLL.setVisibility(View.GONE);
                grandTotal = amount;
                mPresenter.removeCouponCode(OrderPreviewActivity.this);
                if (taxType == 1) {
                    taxAmountTV.setText("₹ " + String.format("%.2f", taxBeforeCoupon));
                    subtotalAmountTV.setText("₹ " + String.format("%.2f", subTotalBeforeCoupon + taxBeforeCoupon));
                    totalAmountTV.setText("₹ " + String.format("%.2f", totalBeforeCoupon));
                } else if (taxType == 0) {
                    taxAmountTV.setText("₹ " + String.format("%.2f", taxBeforeCoupon));
                    subtotalAmountTV.setText("₹ " + String.format("%.2f", subTotalBeforeCoupon));
                    totalAmountTV.setText("₹ " + String.format("%.2f", totalBeforeCoupon));
                }
            }
        });

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                mPresenter.createOrder(OrderPreviewActivity.this, courseID, itemType);
                checkoutBtn.setEnabled(false);
                int a=1;
            }
        });

        new MultiTextWatcher()
                .registerEditText(etCouponCode)
                .setCallback(new MultiTextWatcher.TextWatcherWithInstance() {
                    @Override
                    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(EditText editText, Editable editable) {
                        if (editText.getId() == R.id.et_coupon_code) {
                            if (editText.getText().toString().length() > 0) {
                                etCouponCode.setBackgroundDrawable(ContextCompat.getDrawable(OrderPreviewActivity.this, R.drawable.round_edge_layout_coupon_apply));
                                applyButton.setBackgroundDrawable(ContextCompat.getDrawable(OrderPreviewActivity.this, R.drawable.round_edge_apply_button_bg));
                            } else {
                                //applyCouponLayout.setBackgroundDrawable(ContextCompat.getDrawable(TabsHeaderActivity.this, R.drawable-mdpi.apply_coupon_error_bg));
                                //applyButton.setBackgroundDrawable(ContextCompat.getDrawable(TabsHeaderActivity.this, R.drawable-mdpi.round_edge_apply_button_error_bg));
                            }
                        }
                    }
                });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public int getLayout() {
        setTheme(R.style.HomeScreenTheme);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.homeNavigationBarColor));
            getWindow().setStatusBarColor(getResources().getColor(R.color.homeNavigationBarColor));
        }
        return R.layout.activity_order_preview;
    }

    @Override
    public <T> void onSuccess(T type) {
        onHideProgress();
        if (type instanceof ApplyCouponResponse) {
            haveCouponTV.setVisibility(View.GONE);
            invalidCouponTV.setVisibility(View.GONE);
            applyCouponLayout.setVisibility(View.GONE);
            appliedCouponDiscountLL.setVisibility(View.VISIBLE);
            int discountPrice = 0;
            if (((ApplyCouponResponse) type).getData().getType().equals("1")) {
                discountPrice = Integer.parseInt(((ApplyCouponResponse) type).getData().getRate());
                couponDiscountPriceTV.setText(" -₹ " + discountPrice);
            } else if (((ApplyCouponResponse) type).getData().getType().equals("0")) {
                int discountRate = Integer.parseInt(((ApplyCouponResponse) type).getData().getRate());
                discountPrice = (amount * discountRate) / 100;
                couponDiscountPriceTV.setText(" -₹ " + discountPrice);
            }
            if (taxType == 1) {
                calculateTotalExclusive(type, discountPrice);
            } else if (taxType == 0) {
                calculateTotalInclusive(type, discountPrice);
            }
        } else if (type instanceof CreateOrderResponse) {
            //updateUI((CourseDetailResponseModel) type);
            //Snackbar.make(haveCouponTV, "Success " + ((CreateOrderResponse) type).getData().getOrder_id(), BaseTransientBottomBar.LENGTH_SHORT).show();
            gotoPayment(((CreateOrderResponse) type).getData().getOrder_id());
        }
    }

    private void gotoPayment(String orderId) {
        progressDialog = new ProgressDialog(OrderPreviewActivity.this, R.style.ProgressDialogStyle);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //progressDialog.show();
        Intent payment = new Intent(OrderPreviewActivity.this, PaymentActivity.class);
        payment.putExtra("name", name);
        payment.putExtra("orderID", orderId);
        payment.putExtra("image", imagePath);
        payment.putExtra("amount", String.valueOf(amount));
        startActivity(payment);
        finish();
        /*Intent payment = new Intent(TabsHeaderActivity.this, OrderPreviewActivity.class);
        payment.putExtra("name", courseNameTV.getText().toString());
        payment.putExtra("orderID", orderId);
        payment.putExtra("image", imagePath);
        payment.putExtra("amount", discountPriceTV.getText().toString());
        payment.putExtra("taxType", "" + taxType);
        payment.putExtra("taxRate", "" + taxRate);
        payment.putExtra("ratingStatus", isRatingEnabled);
        payment.putExtra("rating", courseRatingBar.getRating());
        payment.putExtra("courseID", courseID);
        payment.putExtra("type", itemType);
        //payment.putExtra("appKey",dataCourse.getData().getCourse_key_pass().getKey());
        startActivity(payment);
        *//*progressDialog = new ProgressDialog(TabsHeaderActivity.this, R.style.ProgressDialogStyle);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        OBLogger.e("ID " + orderId);
        Intent payment = new Intent(TabsHeaderActivity.this, PaymentActivity.class);
        payment.putExtra("name", courseNameTV.getText().toString());
        payment.putExtra("orderID", orderId);
        payment.putExtra("image", imagePath);
        OBLogger.e("am " + discountPriceTV.getText().toString());
        payment.putExtra("amount", discountPriceTV.getText().toString());
        startActivity(payment);
        finish();*/
    }

    private void calculateExclusiveTax() {
        double taxAmount = (amount * taxrate) / 100;
        taxAmountTV.setText("₹ " + String.format("%.2f", taxAmount));
        double totalAmount = amount + taxAmount;
        totalAmountTV.setText("₹ " + String.format("%.2f", totalAmount));
        taxTotal = taxAmount;
        subTotal = amount;
        grandTotal = totalAmount;

        subTotalBeforeCoupon = subTotal;
        taxBeforeCoupon = taxAmount;
        totalBeforeCoupon = grandTotal;

    }

    private void calculateInclusiveTax() {
        double taxAmount = ((amount) * taxrate / (100 + taxrate));
        double basePrice = amount - taxAmount;
        taxAmountTV.setText("₹ " + String.format("%.2f", taxAmount));
        //subtotalAmountTV.setText("₹ " + String.format("%.2f", basePrice));
        subtotalAmountTV.setText("₹ " + String.format("%.2f", (double) amount));
        taxLayout.setVisibility(View.GONE);
        taxTotal = taxAmount;
        subTotal = basePrice;
        grandTotal = amount;

        subTotalBeforeCoupon = amount;
        taxBeforeCoupon = taxAmount;
        totalBeforeCoupon = grandTotal;
    }

    private <T> void calculateTotalExclusive(T type, int discountPrice) {
        //subTotal = subTotal - discountPrice;
        if (discountPrice < amount) {
            grandTotal = amount - discountPrice;
            subtotalAmountTV.setText("₹ " + String.format("%.2f", subTotal));
            double taxAmount = (grandTotal * taxrate) / 100;
            taxAmountTV.setText("₹ " + String.format("%.2f", taxAmount));
            double totalAmount = grandTotal + taxAmount;
            totalAmountTV.setText("₹ " + String.format("%.2f", totalAmount));
        }
        else {
            Snackbar.make(subtotalAmountTV,"This coupon can't apply to order!",Snackbar.LENGTH_SHORT).show();
            grandTotal = amount ;
            subtotalAmountTV.setText("₹ " + String.format("%.2f", subTotal));
            double taxAmount = (grandTotal * taxrate) / 100;
            taxAmountTV.setText("₹ " + String.format("%.2f", taxAmount));
            double totalAmount = grandTotal + taxAmount;
            totalAmountTV.setText("₹ " + String.format("%.2f", totalAmount));
        }
    }

    private <T> void calculateTotalInclusive(T type, int discountPrice) {
        //subTotal = subTotal - discountPrice;
        //subtotalAmountTV.setText("₹ " + String.format("%.2f", subTotal));
        //double taxAmount = (subTotal * taxrate) / 100;
        //taxAmountTV.setText("₹ " + String.format("%.2f", taxAmount));
        //double totalAmount = subTotal + taxAmount;
        //totalAmountTV.setText("₹ " + String.format("%.2f", totalAmount));
        if (discountPrice < amount) {
            grandTotal = amount - discountPrice;
            taxAmountTV.setVisibility(View.INVISIBLE);
            subtotalAmountTV.setText("₹ " + String.format("%.2f", (double) amount));
            totalAmountTV.setText("₹ " + String.format("%.2f", grandTotal));
        } else {
            Snackbar.make(subtotalAmountTV, "This coupon can't apply to order!", Snackbar.LENGTH_SHORT).show();
            grandTotal = amount;
            taxAmountTV.setVisibility(View.INVISIBLE);
            subtotalAmountTV.setText("₹ " + String.format("%.2f", (double) amount));
            totalAmountTV.setText("₹ " + String.format("%.2f", grandTotal));
        }
    }

    @Override
    public <T> void onFailure(T type) {
        onHideProgress();
        if (type instanceof String) {
            String message = (String) type;
            etCouponCode.setText("");
            //etCouponCode.setHint(message);
            invalidCouponTV.setVisibility(View.VISIBLE);
            invalidCouponTV.setText(message);
            applyCouponLayout.setVisibility(View.VISIBLE);
            etCouponCode.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.round_edge_layout_coupon_apply_error));
            applyButton.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.round_edge_apply_button_error_bg));
        }
    }

    @Override
    public void setPresenter(OrderPreviewContract.Presenter presenter) {

    }

    @Override
    public void onShowProgress() {
        if (progressDialog != null)
            progressDialog.show();
    }

    @Override
    public void onHideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkoutBtn.setEnabled(true);
        //mPresenter.removeCouponCode(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        onHideProgress();
        mPresenter.stop();
    }

    @Override
    public void onShowAlertDialog(String message) {
        onHideProgress();
    }

    @Override
    public void onServerError(String message) {
    }

    @Override
    public void paymentFailed() {
        mPresenter.removeCouponCode(getApplicationContext());
    }
}
