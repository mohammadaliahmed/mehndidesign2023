package com.zik.mehndi.simple.offline.designs.Activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.zik.mehndi.simple.offline.designs.R;
import com.zik.mehndi.simple.offline.designs.Utils.SharedPrefs;


public class Splash extends AppCompatActivity {


    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    Button start;
    LinearLayout logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo=findViewById(R.id.logo);
        start=findViewById(R.id.start);
        getSupportActionBar().hide();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
        });

        if(!SharedPrefs.getPremium().equalsIgnoreCase("1")) {
            LoadAd();
        }
        Animation bounce;

        bounce = AnimationUtils.loadAnimation(Splash.this, R.anim.btn_press);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(
                0.9, 15);
        bounce.setInterpolator(interpolator);
        logo.startAnimation(bounce);


    }
    class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        @Override
        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time / mAmplitude)
                    * Math.cos(mFrequency * time) + 1);
        }
    }

    private void LoadAd() {

        try {
           loadCallback=new AppOpenAd.AppOpenAdLoadCallback() {
               @Override
               public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                   super.onAdFailedToLoad(loadAdError);
               }

               @Override
               public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                   super.onAdLoaded(appOpenAd);
                   appOpenAd.show(Splash.this);

               }
           };
            AppOpenAd.load((Context) Splash.this, getResources().getString(R.string.open_ad_unit_id),
                    new AdRequest.Builder().build(), 1, loadCallback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}