package com.github.codetanzania.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.codetanzania.feature.initialization.SplashScreenActivity;
import com.github.codetanzania.open311.android.library.models.Reporter;
import com.github.codetanzania.util.LanguageUtils;
import com.github.codetanzania.util.LookAndFeelUtils;
import com.github.codetanzania.util.Util;

import tz.co.codetanzania.R;

public class SettingsActivity extends AppCompatActivity {

    private static final int EDIT_PROFILE_REQUEST = 0;
    private TextView tvUsername;
    private TextView tvPhoneNumber;
    private TextView tvAccountNumber;
    private TextView tvEmail;
    private TextView tvDefaultLanguage;
    private Button btnLogout;

    // Current default language
    private String mCurrentLanguage;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_PROFILE_REQUEST) {
            updateUserProfile();
            // handle scenarios where user may use back arrow to navigate one level up
            if (data != null) {
                mayChangeDefaultLanguage(data
                        .getBooleanExtra(EditSettingsActivity.FLAG_LANGUAGE_CHANGED, false));

                // if account number has changed, require company login to view billing info
                boolean accountChanged = data.getBooleanExtra(EditSettingsActivity.FLAG_ACCOUNT_CHANGED, false);
                if (accountChanged) {
                    Util.requireCompanyLogin(getBaseContext());
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tvUsername = (TextView) findViewById(R.id.tv_UserName);
        tvPhoneNumber = (TextView) findViewById(R.id.tv_UserPhone);
        tvDefaultLanguage = (TextView) findViewById(R.id.tv_DefaultLanguage);
        tvAccountNumber = (TextView) findViewById(R.id.tv_accountNum);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        //        tvLocation = (TextView) findViewById(R.id.tv_UserLocation);

        // read current default language
        mCurrentLanguage = LanguageUtils.withBaseContext(getBaseContext())
                .getDefaultLanguageName();

        // allow user to logout
        btnLogout = (Button) findViewById(R.id.btn_Logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmExit();
            }
        });
        btnLogout.setVisibility(View.VISIBLE);

        // fab goes to edit profile
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_EditProfile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(
                        SettingsActivity.this, EditSettingsActivity.class), EDIT_PROFILE_REQUEST);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupToolbar();
        updateUserProfile();
    }

    private void mayChangeDefaultLanguage(boolean languageChanged) {
        // detect if language was changed before updating user profile
        if (languageChanged) {
            startActivity(new Intent(this, SplashScreenActivity.class));
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        LookAndFeelUtils.setupActionBar(this, toolbar, true);
    }

    private void updateUserProfile() {
        Reporter reporter = Util.getCurrentReporter(this);
        assert reporter != null;

        tvUsername.setText(reporter.getName());
        tvAccountNumber.setText(reporter.getAccount());
        tvEmail.setText(reporter.getEmail());

        tvDefaultLanguage.setText(mCurrentLanguage);

        if (TextUtils.isEmpty(reporter.getPhone())) {
            tvPhoneNumber.setText(R.string.text_empty_phone);
        } else {
            tvPhoneNumber.setText(reporter.getPhone());
        }
    }

    private void confirmExit() {
        Util.confirmExitApp(this);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
