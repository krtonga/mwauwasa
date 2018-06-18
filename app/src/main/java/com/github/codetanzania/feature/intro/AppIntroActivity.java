package com.github.codetanzania.feature.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.codetanzania.feature.initialization.SplashScreenActivity;
import com.github.codetanzania.util.LanguageUtils;
import com.github.paolorotolo.appintro.AppIntro2;

import tz.co.codetanzania.R;

public class AppIntroActivity extends AppIntro2 {


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // apply language changes
        LanguageUtils.withBaseContext(getBaseContext())
                .commitChanges();

        @ColorInt int backgroundColor  = ContextCompat.getColor(this, R.color.iconPrimary);
        @ColorInt int titleColor       = ContextCompat.getColor(this, R.color.white);
        @ColorInt int descriptionColor = ContextCompat.getColor(this, R.color.introText);

        // add slides that shows information to the first timers
        // [1] -- this slide introduces app to user
        addSlide(IntroSlideFragment.newInstance(getString(R.string.intro_title_app_welcome, getString(R.string.app_name)),
                getString(R.string.intro_desc_app_main_features),
                R.drawable.ic_company_circle,
                backgroundColor, titleColor, descriptionColor));

        // [2] -- this slide will also request permission to read user location, and take photos,
        //     -- and recording audio
        addSlide(IntroSlideFragment.newInstance(getString(R.string.intro_title_report_issue),
                getString(R.string.intro_report_issue, getString(R.string.app_name)),
                R.drawable.ic_home_create_issue,
                backgroundColor, titleColor, descriptionColor));

        // [3] -- this activity introduces to the user how he/she receives updates about the issues
        //     -- they previously reported to the municipal water company.
        addSlide(IntroSlideFragment.newInstance(getString(R.string.intro_title_see_your_bill),
                getString(R.string.intro_desc_see_your_bill),
                R.drawable.ic_home_see_my_bill,
                backgroundColor, titleColor, descriptionColor));
    }

    private void startSplashScreenActivity() {
        // start splash screen
        startActivity(new Intent(this, SplashScreenActivity.class));
        // we wont come back here
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_intro;
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // show language chooser dialog
        startSplashScreenActivity();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        onDonePressed(currentFragment);
    }
}
