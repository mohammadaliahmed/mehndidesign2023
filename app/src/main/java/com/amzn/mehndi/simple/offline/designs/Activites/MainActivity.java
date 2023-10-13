package com.amzn.mehndi.simple.offline.designs.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amzn.mehndi.simple.offline.designs.Adapters.CategoriesAdapter;
import com.amzn.mehndi.simple.offline.designs.Models.CategoryModel;
import com.amzn.mehndi.simple.offline.designs.R;
import com.amzn.mehndi.simple.offline.designs.Utils.SharedPrefs;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.QueryPurchasesParams;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<CategoryModel> itemList = new ArrayList<>();
    CategoriesAdapter adapter;
    RecyclerView recycler;
    BillingClient billingClient;

    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = findViewById(R.id.recycler);
        adView = findViewById(R.id.adView);
        getSupportActionBar().setElevation(0);

        if (!SharedPrefs.getPremium().equalsIgnoreCase("1")) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        setUpList();
        adapter = new CategoriesAdapter(this, itemList);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        recycler.setAdapter(adapter);
//        checkSubscription();

    }

    private void setUpList() {
        itemList.add(new CategoryModel("Back Hand", "backhand", R.drawable.backhand7));
        itemList.add(new CategoryModel("Front Hand", "fronthand", R.drawable.fronthand7));
        itemList.add(new CategoryModel("Bridal", "bridal", R.drawable.bridal1));
        itemList.add(new CategoryModel("Foot", "foot", R.drawable.foot1));
        itemList.add(new CategoryModel("Finger", "finger", R.drawable.finger1));
        itemList.add(new CategoryModel("Arm", "arm", R.drawable.arm1));
        itemList.add(new CategoryModel("Gol Tikki", "goltiki", R.drawable.goltiki1));
        itemList.add(new CategoryModel("Eid Special", "eid", R.drawable.eid1));
    }

    void checkSubscription() {
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener((billingResult, list) -> {
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
//                                        remainingMessages.setVisibility(View.GONE);
                                        SharedPrefs.setPremium("1"); // set 1 to activate premium feature
                                    } else {
//                                        remainingMessages.setVisibility(View.VISIBLE);
                                        SharedPrefs.setPremium("0"); // set 0 to de-activate premium feature
//                                        remainingMessages.setVisibility(View.VISIBLE);
                                        startActivity(new Intent(MainActivity.this, SubAct.class));
                                    }
                                }
                            });
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // Handle search icon click event
            startActivity(new Intent(MainActivity.this, Settings.class));
            return true;
        }  if (id == R.id.action_no_ads) {
            startActivity(new Intent(MainActivity.this, SubAct.class));
            // Handle search icon click event
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);


        return true;
    }
}