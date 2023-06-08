package com.zik.mehndi.simple.offline.designs.Utils;

import android.app.Application;
import android.content.Context;

import com.applovin.sdk.AppLovinPrivacySettings;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class ApplicationClass extends Application {
    private static ApplicationClass instance;


    private static AppOpenManager appOpenManager;


    public static ApplicationClass getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppLovinPrivacySettings.setHasUserConsent(true, getApplicationContext());

        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {}
                });

        appOpenManager = new AppOpenManager(this);

    }

    private void loadAd(Context context) {
        // We will implement this below.
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}