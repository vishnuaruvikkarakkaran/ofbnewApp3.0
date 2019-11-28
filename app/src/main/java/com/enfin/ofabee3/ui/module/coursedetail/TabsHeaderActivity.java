package com.enfin.ofabee3.ui.module.coursedetail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.text.Editable;
import android.text.Html;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enfin.ofabee3.BuildConfig;
import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.local.database.OBDBHelper;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.BundleDetailResponseModel;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.CourseDetailResponseModel;
import com.enfin.ofabee3.data.remote.model.createorder.response.CreateOrderResponse;
import com.enfin.ofabee3.data.remote.model.selfenroll.response.SelfEnrollResponse;
import com.enfin.ofabee3.ui.base.BaseActivity;
import com.enfin.ofabee3.ui.module.contentdelivery.ContentDeliveryActivity;
import com.enfin.ofabee3.ui.module.coursedetail.coursedescription.CourseDescriptionFragment;
import com.enfin.ofabee3.ui.module.coursedetail.coursereviews.CourseReviewsFragment;
import com.enfin.ofabee3.ui.module.coursedetail.subject.SubjectFragment;
import com.enfin.ofabee3.ui.module.home.HomeActivity;
import com.enfin.ofabee3.ui.module.home.popularcourses.PopularCourse;
import com.enfin.ofabee3.ui.module.login.LoginActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.ui.module.orderpreview.OrderPreviewActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.MultiTextWatcher;
import com.enfin.ofabee3.utils.NetworkUtil;
import com.enfin.ofabee3.utils.OBLogger;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import hk.ids.gws.android.sclick.SClick;

public class TabsHeaderActivity extends BaseActivity implements CourseCurriculamItemListener, CourseDetailContract.View {
    private static final String TAG = TabsHeaderActivity.class.getSimpleName();
    private TextView haveCouponTV, courseNameTV, validityTV, actualPriceTV, discountPriceTV, asterikTV, taxInfoTV, discountTV;
    private TextView actualPriceTV_, discountTV_, discountPriceTV_;
    private LinearLayout applyCouponLayout;
    private Button previewButton, applyButton, buyNowBtn;
    private TextView buyNowBtnBottom_;
    private LinearLayout buyNowBtnBottom;
    private RelativeLayout buyNowRl;
    private EditText etCouponCode;
    private ImageView shareBtn, courseMainImage;
    private Dialog progressDialog;
    private CourseDetailContract.Presenter mPresenter;
    public static String courseID, itemType;
    private RatingBar courseRatingBar;
    private ViewPager viewPager;
    private LinearLayout headerLL;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private boolean isFree = false;
    private String amount, imagePath, courseName;
    private ImageView hTabHeader;
    private View hTabFooter;
    private AppCompatImageView backIV;
    private double taxRate = 0.0;
    private int taxType;
    private String selfEnrollStatus;
    private boolean selfEnrollExpiry;
    private int accessValidity;
    private boolean isRatingEnabled = false;
    private CourseDetailResponseModel dataCourse;
    private BundleDetailResponseModel dataBundle;
    private boolean isShared = false;
    private String slugID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = AppUtils.showProgressDialog(this);
        mPresenter = new CourseDetailPresenter(this, this);
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        if (data != null) {
            isShared = true;
            OBDBHelper dbHelper = new OBDBHelper(this);
            slugID = data.getLastPathSegment();
            if (dbHelper.isUserLoggedIn()) {
                goToCourseDetail();
            } else {
                goToLogin();
            }
        } else {
            courseID = getIntent().getStringExtra(Constants.COURSE_ID);
            itemType = getIntent().getStringExtra(Constants.TYPE);
            goToCourseDetail();
        }
    }

    private void goToLogin() {
        if (NoInternetActivity.isActive) {
            Intent loginIntent = new Intent(TabsHeaderActivity.this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            finish();
        } else {
            Intent loginIntent = new Intent(TabsHeaderActivity.this, LoginActivity.class);
            //loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            finish();
        }
    }

    private void goToCourseDetail() {
        final Toolbar toolbar = findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        applyButton = findViewById(R.id.apply_button);
        etCouponCode = findViewById(R.id.et_coupon_code);
        haveCouponTV = findViewById(R.id.have_coupon_tv);
        applyCouponLayout = findViewById(R.id.apply_coupon_ll);
        previewButton = findViewById(R.id.previewButton);
        buyNowRl = findViewById(R.id.buyNowButton_rl);
        buyNowBtn = findViewById(R.id.buyNowButton);
        buyNowBtnBottom = findViewById(R.id.button_buy_now_bottom);
        buyNowBtnBottom_ = findViewById(R.id.buyNowButton_);
        shareBtn = findViewById(R.id.iv_share);
        courseNameTV = findViewById(R.id.tv_course_name);
        validityTV = findViewById(R.id.validity_tv);
        actualPriceTV = findViewById(R.id.course_actual_price);
        discountPriceTV = findViewById(R.id.course_discount_price);
        actualPriceTV_ = findViewById(R.id.actual_price_tv_);
        discountPriceTV_ = findViewById(R.id.discount_price_tv_);
        asterikTV = findViewById(R.id.asterik_tv);
        taxInfoTV = findViewById(R.id.inclusive_tax_tv);
        discountTV = findViewById(R.id.course_discount);
        discountTV_ = findViewById(R.id.discount_tv_);
        courseRatingBar = findViewById(R.id.course_rating_bar);
        courseMainImage = findViewById(R.id.course_header_image);
        headerLL = findViewById(R.id.header_ll);
        viewPager = findViewById(R.id.htab_viewpager);
        appBarLayout = findViewById(R.id.htab_appbar);
        hTabHeader = findViewById(R.id.htab_header);
        hTabFooter = findViewById(R.id.htab_footer);
        backIV = findViewById(R.id.toolbar_back);

        haveCouponTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                haveCouponTV.setVisibility(View.INVISIBLE);
                applyCouponLayout.setVisibility(View.VISIBLE);
                previewButton.setVisibility(View.INVISIBLE);
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                finish();
            }
        });

        applyButton.setOnClickListener(view -> {
            if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
            if (etCouponCode.getText().toString().length() < 3) {
                applyCouponLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.apply_coupon_error_bg));
                applyButton.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.round_edge_apply_button_error_bg));
            } else {
                hideKeyboard(TabsHeaderActivity.this);
                AppUtils.showDiscountDialog(TabsHeaderActivity.this, getLayoutInflater(), "Discount");
                haveCouponTV.setVisibility(View.VISIBLE);
                applyCouponLayout.setVisibility(View.GONE);
                previewButton.setVisibility(View.VISIBLE);
            }
        });

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (!SClick.check(SClick.BUTTON_CLICK, 1000)) return;
                buyNowBtn.setEnabled(false);
                if (itemType.equals("course"))
                    courseEnroll();
                else if (itemType.equals("bundle"))
                    bundleEnroll();
                //buyNowBtn.setEnabled(false);
            }
        });

        buyNowRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                buyNowRl.setEnabled(false);
                if (itemType.equals("course"))
                    courseEnroll();
                else if (itemType.equals("bundle"))
                    bundleEnroll();
                //buyNowRl.setEnabled(false);
            }
        });

        buyNowBtnBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                buyNowBtnBottom.setEnabled(false);
                if (itemType.equals("course"))
                    courseEnroll();
                else if (itemType.equals("bundle"))
                    bundleEnroll();
            }
        });

        buyNowBtnBottom_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                buyNowBtnBottom_.setEnabled(false);
                if (itemType.equals("course"))
                    courseEnroll();
                else if (itemType.equals("bundle"))
                    bundleEnroll();
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                if (dataCourse != null) {
                    ShareCompat.IntentBuilder.from(TabsHeaderActivity.this)
                            .setType("text/plain")
                            .setChooserTitle("Share Course")
                            .setText(BuildConfig.BASE_URL + dataCourse.getData().getSlug())
                            .startChooser();
                } else if (dataBundle != null) {
                    ShareCompat.IntentBuilder.from(TabsHeaderActivity.this)
                            .setType("text/plain")
                            .setChooserTitle("Share Course")
                            .setText(BuildConfig.BASE_URL + dataBundle.getData().getSlug())
                            .startChooser();
                } else {
                    Toast.makeText(TabsHeaderActivity.this, "Slug is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.isConnected(TabsHeaderActivity.this)) {
                    if (!SClick.check(SClick.BUTTON_CLICK, 2000)) return;
                    gotoFreePreview();
                } else {
                    Toast.makeText(TabsHeaderActivity.this, "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (!isShared)
            mPresenter.getCourseDetail(this, courseID, itemType);
        else
            mPresenter.getSharedCourseDetail(this, slugID, itemType);
    }

    private void bundleEnroll() {
        if (isFree) {
            selfBundleEnroll();
        } else
            createOrder();
    }

    private void courseEnroll() {
        if (selfEnrollStatus.equals("1")) {
            if (!selfEnrollExpiry) {
                if (isFree) {
                    selfEnroll();
                } else
                    createOrder();
            } else {
                onShowAlertDialog("custom_enrollment_expired");
                //Toast.makeText(this, "The student enrollment to the course is expired, Please contact Admin for enrollment !!!", Toast.LENGTH_SHORT).show();
            }
        } else {
            onShowAlertDialog("custom_enrollment_disabled");
            //Toast.makeText(this, "The student enrollment to the course is disabled, Please contact Admin for enrollment !!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void selfEnroll() {
        mPresenter.selfEnroll(this, courseID, "1");
    }

    private void selfBundleEnroll() {
        mPresenter.selfEnroll(this, courseID, "2");
    }

    private int convertToDp(int value) {
        Resources r = this.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value,
                r.getDisplayMetrics()
        );
        return px;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setFooterHeight(int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                convertToDp(height)
        );
        hTabFooter.setLayoutParams(params);
    }

    private void gotoFreePreview() {
        Intent contentDeliveryIntent = new Intent(this, ContentDeliveryActivity.class);
        contentDeliveryIntent.putExtra(Constants.COURSE_ID, courseID);
        startActivity(contentDeliveryIntent);
    }

    @Override
    public int getLayout() {
        setTheme(R.style.CourseDetailScreenTheme);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.homeNavigationBarColor));
            getWindow().setStatusBarColor(getResources().getColor(R.color.homeNavigationBarColor));
        }
        return R.layout.activity_tabs_header;
    }

    private void setupViewPager(ViewPager viewPager, CourseDetailResponseModel data) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new CourseDetailFragment(data), "Topics", data);
        adapter.addFrag(new CourseDescriptionFragment(data), "Summary", data);
        adapter.addFrag(new CourseReviewsFragment(), "Reviews", data);
        viewPager.setAdapter(adapter);
    }

    private void setupBundleViewPager(ViewPager viewPager, BundleDetailResponseModel data) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SubjectFragment(data, Constants.NOT_PURCHASED), "Subject", data);
        adapter.addFrag(new CourseDescriptionFragment(data), "Summary", data);
        adapter.addFrag(new CourseReviewsFragment(), "Reviews", data);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClickListener(int position) {
        OBLogger.e("listener ");
    }

    private void updateUI(CourseDetailResponseModel data) {
        headerLL.setVisibility(View.VISIBLE);
        courseNameTV.setText(Html.fromHtml((data).getData().getCb_title()));
        selfEnrollStatus = data.getData().getCb_has_self_enroll();
        selfEnrollExpiry = data.getData().isSelf_enroll_expired();
        if (data.getData().getCb_is_free().equals("1")) {
            isFree = true;
            previewButton.setVisibility(View.GONE);
        }
        imagePath = data.getData().getCb_image();
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Glide.with(this)
                .load(data.getData().getCb_image())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(courseMainImage);
        if (data.getData().getCb_has_rating().equals("1")) {
            isRatingEnabled = true;
            courseRatingBar.setVisibility(View.VISIBLE);
            if (Float.parseFloat("" + data.getData().getRating()) > 0) {
                courseRatingBar.setRating(Float.parseFloat("" + data.getData().getRating()));
            } else
                courseRatingBar.setRating(0f);
        } else {
            courseRatingBar.setVisibility(View.INVISIBLE);
            isRatingEnabled = false;
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //params.setMargins(10, 10, 10, 10);
            //validityTV.setLayoutParams(params);
            //shareBtn.setLayoutParams(params);
        }
        if (data.getData().getCb_preview().equals("1")) {
            setFooterHeight(200);
            previewButton.setVisibility(View.VISIBLE);
        } else {
            setFooterHeight(140);
            previewButton.setVisibility(View.GONE);
        }
        if (isFree) {
            buyNowBtn.setText("Enroll");
            setFooterHeight(140);
            previewButton.setVisibility(View.GONE);
        }

        updateTablayout(data);
        setTaxType(data);
        setAccessValidity(data);

        if (selfEnrollStatus.equals("0")) {
            buyNowBtn.setAlpha(0.5f);
            buyNowRl.setAlpha(0.5f);
            buyNowBtnBottom.setAlpha(0.5f);
            buyNowBtnBottom_.setAlpha(0.5f);
        } else {
            if (data.getData().isSelf_enroll_expired()) {
                String buttonText;
                if (isFree)
                    buttonText = "Enroll";
                else
                    buttonText = "Buy Now";

                buyNowBtn.setAlpha(0.5f);
                buyNowBtn.setText(buttonText);
                buyNowRl.setAlpha(0.5f);
                buyNowBtnBottom.setAlpha(0.5f);
                buyNowBtnBottom_.setAlpha(0.5f);
                buyNowBtnBottom_.setText(buttonText);
                //buyNowBtn.setEnabled(false);
                //buyNowRl.setEnabled(false);
                // buyNowBtnBottom_.setEnabled(false);
                // buyNowBtnBottom.setEnabled(false);
            } else
                buyNowBtn.setAlpha(1f);
        }

        if (accessValidity == 2) {
            if (data.getData().isAccess_expired()) {
                buyNowBtn.setAlpha(0.5f);
                buyNowBtn.setText("Expired");
                buyNowRl.setAlpha(0.5f);
                buyNowBtnBottom.setAlpha(0.5f);
                buyNowBtnBottom_.setAlpha(0.5f);
                buyNowBtnBottom_.setText("Expired");
                buyNowBtn.setEnabled(false);
                buyNowRl.setEnabled(false);
                buyNowBtnBottom_.setEnabled(false);
                buyNowBtnBottom.setEnabled(false);
            }
        }

        if (data.getData().getCb_status().equals("0")) {
            buyNowBtn.setAlpha(0.5f);
            buyNowBtn.setText("Inactive");
            buyNowRl.setAlpha(0.5f);
            buyNowBtnBottom.setAlpha(0.5f);
            buyNowBtnBottom_.setAlpha(0.5f);
            buyNowBtnBottom_.setText("Inactive");
            buyNowBtn.setEnabled(false);
            buyNowRl.setEnabled(false);
            buyNowBtnBottom_.setEnabled(false);
            buyNowBtnBottom.setEnabled(false);
        }

        if (data.getData().getCb_deleted().equals("1")) {
            buyNowBtn.setAlpha(0.5f);
            buyNowBtn.setText("Deleted");
            buyNowRl.setAlpha(0.5f);
            buyNowBtnBottom.setAlpha(0.5f);
            buyNowBtnBottom_.setAlpha(0.5f);
            buyNowBtnBottom_.setText("Deleted");
            buyNowBtn.setEnabled(false);
            buyNowRl.setEnabled(false);
            buyNowBtnBottom_.setEnabled(false);
            buyNowBtnBottom.setEnabled(false);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    OBLogger.e("Collapsed");
                    if (!isFree)
                        buyNowBtnBottom.setVisibility(View.VISIBLE);
                    else {
                        buyNowBtnBottom.setVisibility(View.VISIBLE);
                    }
                } else {
                    OBLogger.e("Expnded");
                    buyNowBtnBottom.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updateBundleUI(BundleDetailResponseModel data) {
        headerLL.setVisibility(View.VISIBLE);
        courseNameTV.setText(Html.fromHtml((data).getData().getC_title()));
        //selfEnrollStatus = data.getData().getCb_has_self_enroll();
        if (data.getData().getC_is_free().equals("1")) {
            isFree = true;
            previewButton.setVisibility(View.GONE);
            setFooterHeight(140);
        }

        imagePath = data.getData().getC_image();
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(getApplicationContext())
                .load(data.getData().getC_image())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(courseMainImage);

        if (data.getData().getC_rating_enabled().equals("1")) {
            courseRatingBar.setVisibility(View.VISIBLE);
            isRatingEnabled = true;
            if (Float.parseFloat("" + data.getData().getRating()) > 0) {
                courseRatingBar.setRating(Float.parseFloat("" + data.getData().getRating()));
            } else
                courseRatingBar.setRating(0f);
        } else {
            courseRatingBar.setVisibility(View.INVISIBLE);
            isRatingEnabled = false;
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //params.setMargins(0, 0, 0, 0);
            //validityTV.setLayoutParams(params);
        }
        updateBundleTablayout(data);
        setBundleTaxType(data);
        setBundleAccessValidity(data);

        if (isFree) {
            buyNowBtn.setText("Enroll");
        }
        previewButton.setVisibility(View.GONE);
        setFooterHeight(140);

        /*if (selfEnrollStatus.equals("0")) {
            buyNowBtn.setAlpha(0.5f);
            buyNowRl.setAlpha(0.5f);
            buyNowBtnBottom.setAlpha(0.5f);
            buyNowBtnBottom_.setAlpha(0.5f);
        } else {
            if (data.getData().isSelf_enroll_expired()){
                buyNowBtn.setAlpha(0.5f);
                buyNowBtn.setText("Expired");
                buyNowRl.setAlpha(0.5f);
                buyNowBtnBottom.setAlpha(0.5f);
                buyNowBtnBottom_.setAlpha(0.5f);
                buyNowBtnBottom_.setText("Expired");
                buyNowBtn.setEnabled(false);
                buyNowRl.setEnabled(false);
                buyNowBtnBottom_.setEnabled(false);
                buyNowBtnBottom.setEnabled(false);
            }
            else
                buyNowBtn.setAlpha(1f);
        }*/

        if (accessValidity == 2) {
            if (data.getData().isAccess_expired()) {
                buyNowBtn.setAlpha(0.5f);
                buyNowBtn.setText("Expired");
                buyNowRl.setAlpha(0.5f);
                buyNowBtnBottom.setAlpha(0.5f);
                buyNowBtnBottom_.setAlpha(0.5f);
                buyNowBtnBottom_.setText("Expired");
                buyNowBtn.setEnabled(false);
                buyNowRl.setEnabled(false);
                buyNowBtnBottom_.setEnabled(false);
                buyNowBtnBottom.setEnabled(false);
            }
        }

        if (data.getData().getC_status().equals("0")) {
            buyNowBtn.setAlpha(0.5f);
            buyNowBtn.setText("Inactive");
            buyNowRl.setAlpha(0.5f);
            buyNowBtnBottom.setAlpha(0.5f);
            buyNowBtnBottom_.setAlpha(0.5f);
            buyNowBtnBottom_.setText("Inactive");
            buyNowBtn.setEnabled(false);
            buyNowRl.setEnabled(false);
            buyNowBtnBottom_.setEnabled(false);
            buyNowBtnBottom.setEnabled(false);
        }

        if (data.getData().getC_deleted().equals("1")) {
            buyNowBtn.setAlpha(0.5f);
            buyNowBtn.setText("Deleted");
            buyNowRl.setAlpha(0.5f);
            buyNowBtnBottom.setAlpha(0.5f);
            buyNowBtnBottom_.setAlpha(0.5f);
            buyNowBtnBottom_.setText("Deleted");
            buyNowBtn.setEnabled(false);
            buyNowRl.setEnabled(false);
            buyNowBtnBottom_.setEnabled(false);
            buyNowBtnBottom.setEnabled(false);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    OBLogger.e("Collapsed");
                    if (!isFree)
                        buyNowBtnBottom.setVisibility(View.VISIBLE);
                    else
                        buyNowBtnBottom.setVisibility(View.VISIBLE);
                } else {
                    OBLogger.e("Expnded");
                    buyNowBtnBottom.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setTaxType(CourseDetailResponseModel data) {
        switch (Integer.parseInt(data.getData().getCb_tax_method())) {
            case 0:
                taxInfoTV.setText("*Inclusive of all taxes");
                taxRate = (double) data.getData().getTax_details().getCgst() + (double) data.getData().getTax_details().getCgst();
                taxType = 0;
                break;
            case 1:
                taxInfoTV.setText("*Exclusive of all taxes");
                taxRate = (double) data.getData().getTax_details().getCgst() + (double) data.getData().getTax_details().getCgst();
                taxType = 1;
                break;
        }
    }

    private void setBundleTaxType(BundleDetailResponseModel data) {
        switch (Integer.parseInt(data.getData().getC_tax_method())) {
            case 0:
                taxInfoTV.setText("*Inclusive of all taxes");
                taxRate = (double) data.getData().getTax_details().getCgst() + (double) data.getData().getTax_details().getCgst();
                taxType = 0;
                break;
            case 1:
                taxInfoTV.setText("*Exclusive of all taxes");
                taxRate = (double) data.getData().getTax_details().getCgst() + (double) data.getData().getTax_details().getCgst();
                taxType = 1;
                break;
        }
    }

    private void updateTablayout(CourseDetailResponseModel data) {

        setupViewPager(viewPager, data);

        TabLayout tabLayout = findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);

        collapsingToolbarLayout = findViewById(R.id.htab_collapse_toolbar);

        /*try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable-mdpi.course_sample);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {
                    int vibrantColor = palette.getVibrantColor(R.color.primary_500);
                    int vibrantDarkColor = palette.getDarkVibrantColor(R.color.primary_700);
                    //collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                    collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
                }
            });

        } catch (Exception e) {
            // if Bitmap fetch fails, fallback to primary colors
            collapsingToolbarLayout.setContentScrimColor(
                    ContextCompat.getColor(this, R.color.primary_500)
            );
            collapsingToolbarLayout.setStatusBarScrimColor(
                    ContextCompat.getColor(this, R.color.primary_700)
            );
        }*/

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
                            if (editText.getText().toString().length() > 3) {
                                applyCouponLayout.setBackgroundDrawable(ContextCompat.getDrawable(TabsHeaderActivity.this, R.drawable.course_preview_bg));
                                applyButton.setBackgroundDrawable(ContextCompat.getDrawable(TabsHeaderActivity.this, R.drawable.round_edge_apply_button_bg));
                            } else {
                                //applyCouponLayout.setBackgroundDrawable(ContextCompat.getDrawable(TabsHeaderActivity.this, R.drawable-mdpi.apply_coupon_error_bg));
                                //applyButton.setBackgroundDrawable(ContextCompat.getDrawable(TabsHeaderActivity.this, R.drawable-mdpi.round_edge_apply_button_error_bg));
                            }
                        }
                    }
                });
        changeTabsFont(tabLayout);
    }

    private void updateBundleTablayout(BundleDetailResponseModel data) {
        setupBundleViewPager(viewPager, data);
        TabLayout tabLayout = findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.htab_collapse_toolbar);

        /*try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable-mdpi.course_sample);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {
                    int vibrantColor = palette.getVibrantColor(R.color.primary_500);
                    int vibrantDarkColor = palette.getDarkVibrantColor(R.color.primary_700);
                    //collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                    collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
                }
            });

        } catch (Exception e) {
            // if Bitmap fetch fails, fallback to primary colors
            collapsingToolbarLayout.setContentScrimColor(
                    ContextCompat.getColor(this, R.color.primary_500)
            );
            collapsingToolbarLayout.setStatusBarScrimColor(
                    ContextCompat.getColor(this, R.color.primary_700)
            );
        }*/

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
                            if (editText.getText().toString().length() > 3) {
                                applyCouponLayout.setBackgroundDrawable(ContextCompat.getDrawable(TabsHeaderActivity.this, R.drawable.course_preview_bg));
                                applyButton.setBackgroundDrawable(ContextCompat.getDrawable(TabsHeaderActivity.this, R.drawable.round_edge_apply_button_bg));
                            } else {
                                //applyCouponLayout.setBackgroundDrawable(ContextCompat.getDrawable(TabsHeaderActivity.this, R.drawable-mdpi.apply_coupon_error_bg));
                                //applyButton.setBackgroundDrawable(ContextCompat.getDrawable(TabsHeaderActivity.this, R.drawable-mdpi.round_edge_apply_button_error_bg));
                            }
                        }
                    }
                });
        changeTabsFont(tabLayout);
    }

    private void setAccessValidity(CourseDetailResponseModel data) {
        switch (Integer.parseInt(data.getData().getCb_access_validity())) {
            case 0:
                validityTV.setText("Validity: Lifetime");
                //asterikTV.setVisibility(View.INVISIBLE);
                //taxInfoTV.setVisibility(View.INVISIBLE);
                //haveCouponTV.setVisibility(View.INVISIBLE);
                //discountTV.setVisibility(View.INVISIBLE);
                //discountTV_.setVisibility(View.INVISIBLE);
                updatePriceView(data);
                //isFree = true;
                //discountPriceTV.setText((Html.fromHtml("<font color=#8bc83f>FREE</font>")));
                //discountPriceTV_.setText((Html.fromHtml("<font color=#8bc83f>FREE</font>")));
                //buyNowBtn.setText("Enroll");
                //validityTV.setText("Validity: Lifetime");
                accessValidity = 0;
                break;

            case 1:
                updatePriceView(data);
                validityTV.setText("Validity: " + data.getData().getCb_validity() + " days");
                accessValidity = 1;
                break;

            case 2:
                updatePriceView(data);
                validityTV.setText("Validity: " + data.getData().getCb_validity_date());
                accessValidity = 2;
                break;

            default:
                updatePriceView(data);
                validityTV.setText("Validity: NA");
                break;
        }
    }

    private void setBundleAccessValidity(BundleDetailResponseModel data) {
        switch (Integer.parseInt(data.getData().getC_access_validity())) {
            case 0:
                //asterikTV.setVisibility(View.INVISIBLE);
                //taxInfoTV.setVisibility(View.INVISIBLE);
                //haveCouponTV.setVisibility(View.INVISIBLE);
                //discountTV.setVisibility(View.INVISIBLE);
                //discountTV_.setVisibility(View.INVISIBLE);
                //isFree=true;
                //discountPriceTV.setText((Html.fromHtml("<font color=#8bc83f>FREE</font>")));
                //discountPriceTV_.setText((Html.fromHtml("<font color=#8bc83f>FREE</font>")));
                //buyNowBtn.setText("Enroll");
                updateBundlePriceView(data);
                validityTV.setText("Validity: Lifetime");
                accessValidity = 0;
                break;

            case 1:
                updateBundlePriceView(data);
                validityTV.setText("Validity: " + data.getData().getC_validity() + " days");
                accessValidity = 1;
                break;

            case 2:
                updateBundlePriceView(data);
                validityTV.setText("Validity: " + data.getData().getC_validity_date());
                accessValidity = 2;
                break;

            default:
                updateBundlePriceView(data);
                validityTV.setText("Validity: NA");
                break;
        }
    }

    private void updatePriceView(CourseDetailResponseModel data) {
        int salePrice = Integer.parseInt(data.getData().getCb_discount());
        int oldPrice = Integer.parseInt(data.getData().getCb_price());

        if (salePrice > 0) {
            discountPriceTV.setText("₹ " + (data).getData().getCb_discount());
            discountPriceTV_.setText("₹ " + (data).getData().getCb_discount());
            if (oldPrice > 0 && oldPrice != salePrice) {
                actualPriceTV.setText("₹ " + (data).getData().getCb_price());
                actualPriceTV_.setText("₹ " + (data).getData().getCb_price());
            } else {
                actualPriceTV.setVisibility(View.INVISIBLE);
                actualPriceTV_.setVisibility(View.INVISIBLE);
            }
        } else if (salePrice == 0 && oldPrice > 0) {
            discountPriceTV.setText("₹ " + (data).getData().getCb_price());
            discountPriceTV_.setText("₹ " + (data).getData().getCb_price());
        } else {
            asterikTV.setVisibility(View.INVISIBLE);
            taxInfoTV.setVisibility(View.INVISIBLE);
            haveCouponTV.setVisibility(View.INVISIBLE);
            discountTV.setVisibility(View.INVISIBLE);
            discountTV_.setVisibility(View.INVISIBLE);
            discountPriceTV.setText((Html.fromHtml("<font color=#8bc83f>FREE</font>")));
            discountPriceTV_.setText((Html.fromHtml("<font color=#8bc83f>FREE</font>")));
            isFree = true;
            buyNowBtn.setText("Enroll");
            buyNowBtnBottom_.setText("Enroll");
        }

        if (Integer.parseInt(data.getData().getCb_price()) > 0 && Integer.parseInt(data.getData().getCb_discount()) > 0) {
            float discount = Float.parseFloat(data.getData().getCb_price()) - Float.parseFloat(data.getData().getCb_discount());
            float discountPercentage = discount / Float.parseFloat(data.getData().getCb_price());
            discountTV.setText((int) (discountPercentage * 100) + "% OFF");
            discountTV_.setText((int) (discountPercentage * 100) + "% OFF");
        } else {
            discountTV.setVisibility(View.INVISIBLE);
            discountTV_.setVisibility(View.INVISIBLE);
        }
        if (isFree) {
            asterikTV.setVisibility(View.INVISIBLE);
            taxInfoTV.setVisibility(View.INVISIBLE);
            haveCouponTV.setVisibility(View.INVISIBLE);
            discountTV.setVisibility(View.INVISIBLE);
            discountTV_.setVisibility(View.INVISIBLE);
            actualPriceTV_.setVisibility(View.INVISIBLE);
            actualPriceTV.setVisibility(View.INVISIBLE);
            discountPriceTV.setText((Html.fromHtml("<font color=#8bc83f>FREE</font>")));
            discountPriceTV_.setText((Html.fromHtml("<font color=#8bc83f>FREE</font>")));
            buyNowBtn.setText("Enroll");
            buyNowBtnBottom_.setText("Enroll");
        }
    }

    private void updateBundlePriceView(BundleDetailResponseModel data) {

        int salePrice = Integer.parseInt(data.getData().getC_discount());
        int oldPrice = Integer.parseInt(data.getData().getC_price());

        if (salePrice > 0) {
            discountPriceTV.setText("₹ " + (data).getData().getC_discount());
            discountPriceTV_.setText("₹ " + (data).getData().getC_discount());
            if (oldPrice > 0 && oldPrice != salePrice) {
                actualPriceTV.setText("₹ " + (data).getData().getC_price());
                actualPriceTV_.setText("₹ " + (data).getData().getC_price());
            } else {
                actualPriceTV.setVisibility(View.INVISIBLE);
                actualPriceTV_.setVisibility(View.INVISIBLE);
            }
        } else if (salePrice == 0 && oldPrice > 0) {
            discountPriceTV.setText("₹ " + (data).getData().getC_price());
            discountPriceTV_.setText("₹ " + (data).getData().getC_price());
        } else {
            asterikTV.setVisibility(View.INVISIBLE);
            taxInfoTV.setVisibility(View.INVISIBLE);
            haveCouponTV.setVisibility(View.INVISIBLE);
            discountTV.setVisibility(View.INVISIBLE);
            discountTV_.setVisibility(View.INVISIBLE);
            discountPriceTV.setText((Html.fromHtml("<font color=#8bc83f>FREE</font>")));
            discountPriceTV_.setText((Html.fromHtml("<font color=#8bc83f>FREE</font>")));
            isFree = true;
            buyNowBtn.setText("Enroll");
            buyNowBtnBottom_.setText("Enroll");
        }

        if (Integer.parseInt(data.getData().getC_price()) > 0 && Integer.parseInt(data.getData().getC_discount()) > 0) {
            float discount = Float.parseFloat(data.getData().getC_price()) - Float.parseFloat(data.getData().getC_discount());
            float discountPercentage = discount / Float.parseFloat(data.getData().getC_price());
            discountTV.setText((int) (discountPercentage * 100) + "% OFF");
            discountTV_.setText((int) (discountPercentage * 100) + "% OFF");
        } else {
            discountTV.setVisibility(View.INVISIBLE);
            discountTV_.setVisibility(View.INVISIBLE);
        }

        if (isFree) {
            actualPriceTV_.setVisibility(View.INVISIBLE);
            actualPriceTV.setVisibility(View.INVISIBLE);
            asterikTV.setVisibility(View.INVISIBLE);
            taxInfoTV.setVisibility(View.INVISIBLE);
            haveCouponTV.setVisibility(View.INVISIBLE);
            discountTV.setVisibility(View.INVISIBLE);
            discountTV_.setVisibility(View.INVISIBLE);
            discountPriceTV.setText((Html.fromHtml("<font color=#8bc83f>FREE</font>")));
            discountPriceTV_.setText((Html.fromHtml("<font color=#8bc83f>FREE</font>")));
            buyNowBtn.setText("Enroll");
            buyNowBtnBottom_.setText("Enroll");
        }

    }

    private void createOrder() {
        if (!isFree) {
            //place order API
            String type = "0";
            if (itemType.equals("course"))
                type = "1";
            else if (itemType.equals("bundle"))
                type = "2";
            gotoPayment(courseID, type);
            //mPresenter.createOrder(this, courseID, type);
            //progressDialog = new ProgressDialog(TabsHeaderActivity.this, R.style.ProgressDialogStyle);
            //progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            //progressDialog.show();
            //Intent payment = new Intent(TabsHeaderActivity.this, PaymentActivity.class);
            //payment.putExtra("name", courseNameTV.getText().toString());
            //payment.putExtra("image", imagePath);
            //OBLogger.e("am " + discountPriceTV.getText().toString());
            //payment.putExtra("amount", discountPriceTV.getText().toString());
            //startActivity(payment);
            //finish();
        } else {
            Toast.makeText(TabsHeaderActivity.this, "Please contact admin for enrolling to this course", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T> void onSuccess(T type) {
        if (type instanceof CourseDetailResponseModel) {
            onHideProgress();
            dataCourse = (CourseDetailResponseModel) type;
            updateUI((CourseDetailResponseModel) type);
            if (dataCourse != null) {
                String apikey = decodeBase64(dataCourse.getData().getCourse_key_pass().getKey());
                Prefs.putString(Constants.APIKEY, apikey);
            }
            //Snackbar.make(haveCouponTV, "Success", BaseTransientBottomBar.LENGTH_SHORT).show();
        } else if (type instanceof BundleDetailResponseModel) {
            onHideProgress();
            dataBundle = (BundleDetailResponseModel) type;
            updateBundleUI((BundleDetailResponseModel) type);
            if (dataBundle != null) {
                String apikey = decodeBase64(dataBundle.getData().getCourse_key_pass().getKey());
                Prefs.putString(Constants.APIKEY, apikey);
            }
            //Snackbar.make(haveCouponTV, "Success", BaseTransientBottomBar.LENGTH_SHORT).show();
        } else if (type instanceof CreateOrderResponse) {
            //updateUI((CourseDetailResponseModel) type);
            //Snackbar.make(haveCouponTV, "Success " + ((CreateOrderResponse) type).getData().getOrder_id(), BaseTransientBottomBar.LENGTH_SHORT).show();
            //onHideProgress();
            gotoPayment(((CreateOrderResponse) type).getData().getOrder_id());
        }
    }

    private void gotoPayment(String orderId) {
        Intent payment = new Intent(TabsHeaderActivity.this, OrderPreviewActivity.class);
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
        startActivity(payment);
        /*progressDialog = new ProgressDialog(TabsHeaderActivity.this, R.style.ProgressDialogStyle);
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

    private void gotoPayment(String courseId, String itemType) {
        Intent payment = new Intent(TabsHeaderActivity.this, OrderPreviewActivity.class);
        payment.putExtra("name", courseNameTV.getText().toString());
        payment.putExtra("orderID", courseId);
        payment.putExtra("image", imagePath);
        payment.putExtra("amount", discountPriceTV.getText().toString());
        payment.putExtra("taxType", "" + taxType);
        payment.putExtra("taxRate", "" + taxRate);
        payment.putExtra("ratingStatus", isRatingEnabled);
        payment.putExtra("rating", courseRatingBar.getRating());
        payment.putExtra("courseID", courseId);
        payment.putExtra("type", itemType);
        //payment.putExtra("appKey",dataCourse.getData().getCourse_key_pass().getKey());
        startActivity(payment);
        /*progressDialog = new ProgressDialog(TabsHeaderActivity.this, R.style.ProgressDialogStyle);
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

    private String decodeBase64(String coded) {
        byte[] valueDecoded = new byte[0];
        try {
            valueDecoded = Base64.decode(coded.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
        }
        return new String(valueDecoded);
    }

    @Override
    public void invokeLogin() {
        onHideProgress();
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.putExtra(Constants.COURSE_ID, courseID);
        loginIntent.putExtra(Constants.TYPE, itemType);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(loginIntent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {

        }
    }

    @Override
    public void onSuccessSelfEnroll(SelfEnrollResponse response) {
        //onHideProgress();
        Toast.makeText(this, "You have been successfully subscribed to this course", Toast.LENGTH_LONG).show();
        Intent home = new Intent(TabsHeaderActivity.this, HomeActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        home.putExtra(Constants.DESTINATION, "mycourse");
        startActivity(home);
        finish();
    }

    @Override
    public <T> void onFailure(T type) {
        onHideProgress();
        Snackbar.make(haveCouponTV, "Error", BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(CourseDetailContract.Presenter presenter) {
        this.mPresenter = presenter;
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
    public void onShowAlertDialog(String message) {
        onHideProgress();
        if (message.equalsIgnoreCase("No Internet")) {
            Intent noInternet = new Intent(this, NoInternetActivity.class);
            startActivity(noInternet);
        } else if (message.equals("custom_enrollment_disabled")) {
            AppUtils.onShowSnackbar(buyNowBtn, "Student enrollment to the course is disabled by Admin!");
        } else if (message.equals("custom_enrollment_expired")) {
            AppUtils.onShowSnackbar(buyNowBtn, "The student enrollment to the course is expired, Please contact Admin for enrollment !");
        } else
            AppUtils.onShowAlertDialog(this, message);
    }

    @Override
    public void onServerError(String message) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    protected void onResume() {
        OBLogger.e("on resume");
        buyNowBtn.setEnabled(true);
        buyNowRl.setEnabled(true);
        buyNowBtnBottom.setEnabled(true);
        buyNowBtnBottom_.setEnabled(true);
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        /*if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();*/
        //onHideProgress();
        mPresenter.stop();
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private CourseDetailResponseModel courseDetailResponseData;
        private BundleDetailResponseModel bundleDetailResponseData;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title, CourseDetailResponseModel data) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            courseDetailResponseData = data;
        }

        public void addFrag(Fragment fragment, String title, BundleDetailResponseModel data) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            bundleDetailResponseData = data;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void changeTabsFont(TabLayout tabLayout) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    Typeface mTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/opensans_semibold.ttf");
                    ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.BOLD);
                    ((TextView) tabViewChild).setTextSize(30);
                    ((TextView) tabViewChild).setMaxLines(2);
                }
            }
        }
    }
}
