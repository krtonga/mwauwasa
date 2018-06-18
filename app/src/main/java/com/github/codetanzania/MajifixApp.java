package com.github.codetanzania;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.github.codetanzania.core.Analytics;
import com.github.codetanzania.open311.android.library.MajiFix;

import tz.co.codetanzania.BuildConfig;
import tz.co.codetanzania.R;

public class MajifixApp extends Application {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Majifix library
        MajiFix.setup(getApplicationContext(),
                BuildConfig.END_POINT,
                getString(R.string.content_provider));

        // Start analytics and crash reporting
        Analytics.initialize(this);
    }
}
