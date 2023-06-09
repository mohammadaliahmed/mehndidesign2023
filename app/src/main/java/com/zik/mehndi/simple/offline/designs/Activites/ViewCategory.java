package com.zik.mehndi.simple.offline.designs.Activites;

import android.content.Intent;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.zik.mehndi.simple.offline.designs.Adapters.ImagesAdapter;
import com.zik.mehndi.simple.offline.designs.Models.CategoryModel;
import com.zik.mehndi.simple.offline.designs.R;
import com.zik.mehndi.simple.offline.designs.Utils.CommonUtils;
import com.zik.mehndi.simple.offline.designs.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ViewCategory extends AppCompatActivity {

    RecyclerView recycler;
    CategoryModel categoryModel;
    ImagesAdapter adapter;
    private List<Integer> imgsList = new ArrayList<>();
    AdView adView;
    private InterstitialAd interstitialAd;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);

        }
        adView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        if (!SharedPrefs.getPremium().equalsIgnoreCase("1")) {

            adView.loadAd(adRequest);
            LoadInterstritial();
        }
        categoryModel = (CategoryModel) getIntent().getSerializableExtra("category");
        this.setTitle(categoryModel.getCategoryName());

        recycler = findViewById(R.id.recycler);
        setUpList();


        adapter = new ImagesAdapter(this, imgsList, false, new ImagesAdapter.ImagesAdapterCallback() {
            @Override
            public void OnLikedUnliked(Integer img, boolean like) {
                HashMap<Integer, Integer> map = SharedPrefs.getLikedMap();
                if (map == null) {
                    map = new HashMap<>();
                }
                if (like) {
                    map.put(img, img);
                } else {
                    map.remove(img);
                }
                SharedPrefs.setLikedMap(map);
            }
        });
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        recycler.setAdapter(adapter);


    }

    private void setUpList() {
        if (categoryModel.getShortName().equalsIgnoreCase("arm")) {
            for (int i = 1; i <= 17; i++) {
                int resourceId = getResources().getIdentifier("arm" + i, "drawable", getPackageName());
                imgsList.add(resourceId);
            }
        }
        if (categoryModel.getShortName().equalsIgnoreCase("backhand")) {
            for (int i = 1; i <= 20; i++) {
                int resourceId = getResources().getIdentifier("backhand" + i, "drawable", getPackageName());
                imgsList.add(resourceId);
            }
        }

        if (categoryModel.getShortName().equalsIgnoreCase("bridal")) {
            for (int i = 1; i <= 19; i++) {
                int resourceId = getResources().getIdentifier("bridal" + i, "drawable", getPackageName());
                imgsList.add(resourceId);
            }
        }
        if (categoryModel.getShortName().equalsIgnoreCase("eid")) {
            for (int i = 1; i <= 20; i++) {
                int resourceId = getResources().getIdentifier("eid" + i, "drawable", getPackageName());
                imgsList.add(resourceId);
            }
        }
        if (categoryModel.getShortName().equalsIgnoreCase("foot")) {
            for (int i = 1; i <= 25; i++) {
                int resourceId = getResources().getIdentifier("foot" + i, "drawable", getPackageName());
                imgsList.add(resourceId);
            }
        }
        if (categoryModel.getShortName().equalsIgnoreCase("fronthand")) {
            for (int i = 1; i <= 19; i++) {
                int resourceId = getResources().getIdentifier("fronthand" + i, "drawable", getPackageName());
                imgsList.add(resourceId);
            }
        }
        if (categoryModel.getShortName().equalsIgnoreCase("goltiki")) {
            for (int i = 1; i <= 15; i++) {
                int resourceId = getResources().getIdentifier("goltiki" + i, "drawable", getPackageName());
                imgsList.add(resourceId);
            }

        }
        if (categoryModel.getShortName().equalsIgnoreCase("finger")) {
            for (int i = 1; i <= 21; i++) {
                int resourceId = getResources().getIdentifier("finger" + i, "drawable", getPackageName());
                imgsList.add(resourceId);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }
        int id = item.getItemId();
        if (id == R.id.action_liked) {
            startActivity(new Intent(ViewCategory.this, ViewLiked.class));
            // Handle search icon click event
            return true;
        }
        if (id == R.id.action_no_ads) {
            startActivity(new Intent(ViewCategory.this, SubAct.class));
            // Handle search icon click event
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu, menu);


        return true;
    }

    public void LoadInterstritial() {

        InterstitialAd.load(
                this,
                getResources().getString(R.string.interstital_ad_unit_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        ViewCategory.this.interstitialAd = interstitialAd;
                        showInterstitial();
//
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        ViewCategory.this.interstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
//                                        LoadInterstritial();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        ViewCategory.this.interstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
//                                        LoadInterstritial();
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
//                                        LoadInterstritial();
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        interstitialAd = null;
                        LoadInterstritial();

                    }
                });


    }

    private void showInterstitial() {
        CommonUtils.showToast("Loading Ad..");
        // Show the ad if it's ready. Otherwise toast and restart the game.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (interstitialAd != null) {
                    interstitialAd.show(ViewCategory.this);
                } else {
                    LoadInterstritial();
                }
            }
        }, 1500);
    }
}