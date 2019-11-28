package com.enfin.ofabee3.ui.module.Payment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.ui.module.coursedetail.TabsHeaderActivity;
import com.enfin.ofabee3.ui.module.home.HomeActivity;
import com.enfin.ofabee3.ui.module.nointernet.NoInternetActivity;
import com.enfin.ofabee3.utils.AppUtils;
import com.enfin.ofabee3.utils.Constants;
import com.enfin.ofabee3.utils.OBLogger;
import com.pixplicity.easyprefs.library.Prefs;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener, PaymentContract.View {

    private static final String TAG = PaymentActivity.class.getSimpleName();
    private String imagePath, name, orderID, apikey;
    private int amount;
    private PaymentContract.Presenter mPresenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        imagePath = getIntent().getStringExtra("image");
        name = getIntent().getStringExtra("name");
        orderID = getIntent().getStringExtra("orderID");
        amount = Integer.parseInt(getIntent().getStringExtra("amount"));
        apikey = Prefs.getString(Constants.APIKEY, "");
        mPresenter = new PaymentPresenter(this, this);
        /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());
        startPayment();
        // Payment button created by you in XML layout
        Button buttonCheckOut = findViewById(R.id.btn_pay);

        //buttonCheckOut.setOnClickListener(v -> startPayment());

        ImageView close = findViewById(R.id.close_btn);
        close.setOnClickListener(view -> {
            Toast.makeText(this, "Payment UnSuccessful: ", Toast.LENGTH_SHORT).show();
            Intent home = new Intent(PaymentActivity.this, HomeActivity.class);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //startActivity(home);
            finish();
        });
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */

        final Activity activity = this;
        final Checkout co = new Checkout();
        //co.setImage(LoadImageFromWebURL(imagePath));
        //rzp_test_9vfuxilMJp7h7O
        co.setKeyID(apikey);
        try {
            JSONObject options = new JSONObject();
            options.put("name", name);
            options.put("description", "");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", imagePath);
            options.put("order_id", orderID);
            options.put("currency", "INR");
            options.put("amount", amount * 100);
            JSONObject preFill = new JSONObject();
            //preFill.put("email", "");
            //preFill.put("contact", "");
            //options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    public static Drawable LoadImageFromWebURL(String url) {
        try {
            InputStream iStream = (InputStream) new URL(url).getContent();
            Drawable drawable = Drawable.createFromStream(iStream, "name");
            return drawable;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPresenter.removeCouponCode(getApplicationContext());
        Toast.makeText(this, "Payment UnSuccessful: ", Toast.LENGTH_SHORT).show();
        Intent home = new Intent(PaymentActivity.this, HomeActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //startActivity(home);
        finish();
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData data) {
        try {
            Toast.makeText(this, "Payment Successful!! ReferenceID: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            String paymentId = data.getPaymentId();
            String signature = data.getSignature();
            String orderId = data.getOrderId();
            String type = "0";
            if (TabsHeaderActivity.itemType.equals("course"))
                type = "1";
            else if (TabsHeaderActivity.itemType.equals("bundle"))
                type = "2";
            mPresenter.completePayment(this, TabsHeaderActivity.courseID, type, orderId, paymentId, signature);
        } catch (Exception e) {
            Log.e(TAG, "Payment failed", e);

        }
    }

    @Override
    public void onPaymentError(int code, String response, PaymentData data) {
        try {
            mPresenter.removeCouponCode(getApplicationContext());
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            //Intent home = new Intent(PaymentActivity.this, HomeActivity.class);
            //home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //startActivity(home);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    @Override
    public <T> void onSuccess(T type) {
        onShowProgress();
        Intent home = new Intent(PaymentActivity.this, HomeActivity.class);
        home.putExtra(Constants.DESTINATION, "mycourse");
        home.putExtra(Constants.COURSE_NAME, name);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(home);
        finish();
    }

    @Override
    public <T> void onFailure(T type) {
        OBLogger.e("type  " + type);
        onShowAlertDialog((String) type);
    }

    @Override
    public void setPresenter(PaymentContract.Presenter presenter) {

    }

    @Override
    public void onShowProgress() {
        progressDialog = new ProgressDialog(PaymentActivity.this, R.style.ProgressDialogStyle);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
    }

    @Override
    public void onHideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onShowAlertDialog(String message) {
        if (message.equalsIgnoreCase("No Internet")) {
            Intent noInternet = new Intent(this, NoInternetActivity.class);
            startActivity(noInternet);
        } else
            AppUtils.onShowAlertDialog(this, message);
    }

    @Override
    public void onServerError(String message) {
    }

    @Override
    protected void onStop() {
        onHideProgress();
        super.onStop();
    }
}
