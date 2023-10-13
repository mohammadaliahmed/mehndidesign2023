package com.amzn.mehndi.simple.offline.designs.Utils;

import android.app.Application;
import android.content.Context;


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