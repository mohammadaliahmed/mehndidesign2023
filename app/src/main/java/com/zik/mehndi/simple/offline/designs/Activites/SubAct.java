package com.zik.mehndi.simple.offline.designs.Activites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.ImmutableList;
import com.zik.mehndi.simple.offline.designs.Adapters.SubscriptionAdapter;
import com.zik.mehndi.simple.offline.designs.R;
import com.zik.mehndi.simple.offline.designs.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;


public class SubAct extends AppCompatActivity {

    Activity activity;
    private BillingClient billingClient;
    List<ProductDetails> productDetailsList = new ArrayList<>();
    List<String> productIds;
    ProgressBar loadProducts;
    RecyclerView recyclerView;
    Handler handler;
    com.google.android.material.floatingactionbutton.FloatingActionButton btn_restore_fab;
    SubscriptionAdapter adapter;
    TextView manageSub;
    TextView continuewith;
    TextView terms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subs);
        getSupportActionBar().hide();
        initViews();
        handler = new Handler();

        activity = this;

        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(
                        (billingResult, list) -> {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                                for (Purchase purchase : list) {
                                    verifySubPurchase(purchase);
                                }
                            }
                        }
                ).build();

        establishConnection();

        manageSub.setOnClickListener(v -> {
            showBottomSheetDialog();
        });

    }

    private void initViews() {
        terms = findViewById(R.id.terms);
        continuewith = findViewById(R.id.continuewith);

        manageSub = findViewById(R.id.manageSub);

        productIds = new ArrayList<>();
        productDetailsList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        loadProducts = findViewById(R.id.loadProducts);

        productIds.add(0, "lifetime");

        adapter = new SubscriptionAdapter(getApplicationContext(), productDetailsList, new SubscriptionAdapter.SubscriptionAdapterCallback() {
            @Override
            public void OnPurchase(ProductDetails details) {
                launchPurchaseFlow(details);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(SubAct.this, 1));
        recyclerView.setAdapter(adapter);
        continuewith.setPaintFlags(continuewith.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        continuewith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://logo-maker-3d-24b00-default-rtdb.firebaseio.com/privacyMehndi.json";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

            }
        });


    }

    void establishConnection() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    showLifetimeProducts();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                establishConnection();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void showLifetimeProducts() {
        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(
                //Product 1
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("lifetime")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
        );

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        billingClient.queryProductDetailsAsync(
                params,
                (billingResult, prodDetailsList) -> {
                    // Process the result
                    handler.postDelayed(() -> {
                        loadProducts.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        productDetailsList.add(prodDetailsList.get(0));
                        adapter.setProductDetailsList(productDetailsList);
                    }, 2000);

                }
        );

    }





    private void saveOfferToken(List<ProductDetails> prodDetailsList) {
        if (prodDetailsList.size() == productIds.size()) { // checking if the return products are of the same size we defined in our productId array
            for (int i = 0; i < prodDetailsList.size(); i++) {
                if (prodDetailsList.get(i).getProductId().equals(productIds.get(0))) { // checking productId weekSub
//                    prefs.setString(productIds.get(0) + "_offerToken", prodDetailsList.get(i).getSubscriptionOfferDetails().get(0).getOfferToken());
//                    Log.d("SaveToken", "Weekly OfferToken " + prefs.getString(productIds.get(0) + "_offerToken", ""));
                } else if (prodDetailsList.get(i).getProductId().equals(productIds.get(1))) { // checking productId MontSub
//                    prefs.setString(productIds.get(1) + "_offerToken", prodDetailsList.get(i).getSubscriptionOfferDetails().get(0).getOfferToken());
//                    Log.d("SaveToken", "Monthly OfferToken  " + prefs.getString(productIds.get(1) + "_offerToken", ""));
                }
            }
        }
    }

    void launchPurchaseFlow(ProductDetails productDetails) {
        if (productDetails.getProductId().equals("lifetime")) {
            ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                    ImmutableList.of(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(productDetails)
                                    .build()
                    );
            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build();

            billingClient.launchBillingFlow(activity, billingFlowParams);
        } else {
            ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                    ImmutableList.of(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(productDetails)
                                    .setOfferToken(productDetails.getSubscriptionOfferDetails().get(0).getOfferToken())
                                    .build()
                    );
            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build();

            billingClient.launchBillingFlow(activity, billingFlowParams);
        }
    }

    void verifySubPurchase(Purchase purchases) {
        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams
                .newBuilder()
                .setPurchaseToken(purchases.getPurchaseToken())
                .build();

        billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                //use prefs to set premium
                //Setting premium to 1 or you can use a boolean and set to true
                // 1 - premium
                // 0 - no premium
                SharedPrefs.setPremium("1");

                //Optional but i will explain.
                for (int i = 0; i < productIds.size(); i++) {
                    if (purchases.getProducts().get(0).equals("test_sub_weekly1")) {
//                        prefs.setString("subType", "Weekly Subscription");
                    } else if (purchases.getProducts().get(0).equals("test_sub_monthly1")) {
//                        prefs.setString("subType", "Monthly Subscription");
                    }
                }

                String productId = purchases.getProducts().get(0); /// this one gets the product Id
                String purchaseToken = purchases.getPurchaseToken(); /// this one gets the purchase token

                Log.d("Test12345verifySubPurchase", purchases.toString());
                Log.d("Test12345verifySubPurchase", "Purchase token " + purchaseToken);
                Log.d("Test12345verifySubPurchase", "Product Id " + productId);

                //Save these values for upgrade purposes
//                prefs.setString("purchasedToken", purchases.getPurchaseToken());
//                prefs.setString("purchasedProductId", productId);

//                handler.postDelayed(this::reloadScreen, 2000);
                finish();
            }
        });
    }

    void restorePurchases() {

        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener((billingResult, list) -> {
                }).build();

        final BillingClient finalBillingClient = billingClient;
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    finalBillingClient.queryPurchasesAsync(
                            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(), (billingResult1, list) -> {
                                if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                    if (list.size() > 0) {
                                        Log.d("Test1234restorePurchases", list.get(0).getPurchaseToken()); // This is the OldPurchaseToken
//                                        prefs.setString("purchasedToken", list.get(0).getPurchaseToken());
//                                        prefs.setString("purchasedProductId", list.get(0).getProducts().get(0));
//                                        prefs.setPremium(1); // set 1 to activate premium feature
                                        showSnackBar(btn_restore_fab, "Successfully restored");
                                    } else {
//                                        prefs.setString("subType", "No Subscription");
//                                        prefs.setString("purchasedProductId", "");
//                                        prefs.setString("purchasedToken", "");
                                        showSnackBar(btn_restore_fab, "Oops, No purchase found.");
//                                        prefs.setPremium(0); // set 0 to de-activate premium feature
                                    }
                                }
                            });
                }
            }
        });
    }

    void upgradeOrDowngrade(String dynamicProductId) {

        Log.d("TestUpgrade", "The product list Size " + productDetailsList.size());
        Log.d("TestUpgrade", "The product list Details " + productDetailsList.toString());

        for (ProductDetails newProdDetails : productDetailsList) {

            if (newProdDetails.getProductId().equals(dynamicProductId)) {

                assert newProdDetails
                        .getSubscriptionOfferDetails() != null;

                String offerToken = newProdDetails.getSubscriptionOfferDetails().get(0).getOfferToken();

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(
                                ImmutableList.of(
                                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                                .setProductDetails(newProdDetails)
                                                .setOfferToken(offerToken)
                                                .build()))
                        .setSubscriptionUpdateParams(
                                BillingFlowParams.SubscriptionUpdateParams.newBuilder()
//                                        .setOldPurchaseToken(prefs.getString("purchasedToken", ""))
                                        .setReplaceProrationMode(BillingFlowParams.ProrationMode.IMMEDIATE_AND_CHARGE_FULL_PRICE)
                                        .build())
                        .build();

                //Opening the Billing flow
                billingClient.launchBillingFlow(activity, billingFlowParams);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void showBottomSheetDialog() {


        String url = "https://play.google.com/store/account/orderhistory";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }


    public void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    protected void onResume() {
        super.onResume();
        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(),
                (billingResult, list) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : list) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                                verifySubPurchase(purchase);
                            }
                        }
                    }
                }
        );
    }

    private void reloadScreen() {
        //Reload the screen to activate the removeAd and remove the actual Ad off the screen.
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
    }
}