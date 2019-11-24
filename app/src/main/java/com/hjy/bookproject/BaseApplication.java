package com.hjy.bookproject;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.hjy.bookproject.utils.SPUtils;

public class BaseApplication extends Application {

    private static Application application;

    public static Context getContext() {
        return application.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        SPUtils.createPref(this, "Book");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration newConfig = new Configuration();
        newConfig.setToDefaults();
        res.updateConfiguration(newConfig, res.getDisplayMetrics());
        return res;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
