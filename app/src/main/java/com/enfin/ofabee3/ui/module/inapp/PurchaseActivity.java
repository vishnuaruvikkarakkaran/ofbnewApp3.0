package com.enfin.ofabee3.ui.module.inapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.enfin.ofabee3.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PurchaseActivity extends AppCompatActivity implements View.OnClickListener, PurchasesUpdatedListener {

    private Button buyNow;
    private BillingClient billingClient;
    private boolean isBillingCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        buyNow = findViewById(R.id.buy);
        buyNow.setOnClickListener(this);

        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    isBillingCreated = true;
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                isBillingCreated = false;
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.buy:
                if (isBillingCreated){
                    List<String> skuList = new ArrayList<>();
                    skuList.add("android.test.purchased");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult,
                                                                 List<SkuDetails> skuDetailsList) {
                                    // Process the result.
                                    // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
                                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetailsList.get(0))
                                            .build();
                                    int billingResponseCode = billingClient.launchBillingFlow(PurchaseActivity.this, flowParams).getResponseCode();
                                    if (billingResponseCode == BillingClient.BillingResponseCode.OK) {
                                        // do something you want
                                    }
                                }
                            });

                }
                break;
        }

    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<com.android.billingclient.api.Purchase> purchases) {


        Log.e("getDebugMessage",billingResult.getDebugMessage());
        Log.e("getResponseCode", String.valueOf(billingResult.getResponseCode()));
        Log.e("purchases",new Gson().toJson(purchases));

    }
}
