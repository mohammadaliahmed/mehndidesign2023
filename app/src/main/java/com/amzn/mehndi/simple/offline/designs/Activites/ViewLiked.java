package com.amzn.mehndi.simple.offline.designs.Activites;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.amzn.mehndi.simple.offline.designs.Adapters.ImagesAdapter;
import com.amzn.mehndi.simple.offline.designs.R;
import com.amzn.mehndi.simple.offline.designs.Utils.CommonUtils;
import com.amzn.mehndi.simple.offline.designs.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ViewLiked extends AppCompatActivity {

    RecyclerView recycler;
    ImagesAdapter adapter;
    private List<Integer> imgsList = new ArrayList<>();
    private HashMap<Integer, Integer> map = new HashMap<>();
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
        this.setTitle("Favorites");


        if (!SharedPrefs.getPremium().equalsIgnoreCase("1")) {
            adRequest = new AdRequest.Builder().build();
            LoadInterstritial();
        }

        recycler = findViewById(R.id.recycler);
        map = SharedPrefs.getLikedMap();
        if (map == null) {
            map = new HashMap<>();
        }
        imgsList = new ArrayList<>(map.values());
        adapter = new ImagesAdapter(this, imgsList,true, new ImagesAdapter.ImagesAdapterCallback() {
            @Override
            public void OnLikedUnliked(Integer img, boolean like) {

                map.remove(img);
                SharedPrefs.setLikedMap(map);
                imgsList = new ArrayList<>(map.values());
                adapter.setItemList(imgsList);
                LoadInterstritial();
            }

        });
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        recycler.setAdapter(adapter);
    }
    public void LoadInterstritial() {

        InterstitialAd.load(
                this,
                getResources().getString(R.string.interstital_ad_unit_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd aa) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                       interstitialAd = aa;
                        showInterstitial();
//
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        interstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
//                                        LoadInterstritial();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                       interstitialAd = null;
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
                    interstitialAd.show(ViewLiked.this);
                } else {
                    LoadInterstritial();
                }
            }
        }, 1500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}