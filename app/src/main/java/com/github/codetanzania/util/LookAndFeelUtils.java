package com.github.codetanzania.util;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import tz.co.codetanzania.R;

public class LookAndFeelUtils {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(color);
    }

    public static void setupActionBar(AppCompatActivity activity, int titleRes, boolean displayUpAsHomeEnabled) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.basic_toolbar_layout);
        setupActionBar(activity, toolbar, titleRes, displayUpAsHomeEnabled);
    }

    public static void setupActionBar(AppCompatActivity activity, Toolbar toolbar, boolean displayUpAsHomeEnabled) {
        setupActionBar(activity, toolbar, null, displayUpAsHomeEnabled, null);
    }

    public static void setupActionBar(AppCompatActivity activity, Toolbar toolbar,
                                      String titleRes, boolean displayUpAsHomeEnabled) {
        setupActionBar(activity, toolbar, titleRes, displayUpAsHomeEnabled, null);
    }

    private static void setupActionBar(AppCompatActivity activity, Toolbar toolbar,
                                       int titleRes, boolean displayUpAsHomeEnabled) {
        setupActionBar(activity, toolbar, titleRes, displayUpAsHomeEnabled, null);
    }

    public static void setupActionBar(AppCompatActivity activity, Toolbar toolbar,
                                      int titleRes, boolean displayUpAsHomeEnabled, Integer homeIconRes) {
        setupActionBar(activity, toolbar, activity.getString(titleRes), displayUpAsHomeEnabled, homeIconRes);
    }

    public static void setupActionBar(AppCompatActivity activity, Toolbar toolbar,
                                      String title, boolean displayUpAsHomeEnabled, Integer homeIconRes) {
        assert toolbar != null;
        activity.setSupportActionBar(toolbar);

        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;
        if (title != null) {
            actionBar.setTitle(title);
        }
        actionBar.setDisplayHomeAsUpEnabled(displayUpAsHomeEnabled);
        if (homeIconRes != null) {
            actionBar.setHomeAsUpIndicator(homeIconRes);
        }
    }
}
