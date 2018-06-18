package com.github.codetanzania.feature.initialization;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.github.codetanzania.feature.home.MainActivity;
import com.github.codetanzania.feature.intro.AppIntroActivity;
import com.github.codetanzania.feature.views.SingleItemSelectionDialog;
import com.github.codetanzania.feature.logincitizen.UserDetailsActivity;
import com.github.codetanzania.util.LanguageUtils;
import com.github.codetanzania.feature.logincitizen.PhoneVerificationUtils;
import com.github.codetanzania.util.Util;

import tz.co.codetanzania.R;

public class SplashScreenActivity extends AppCompatActivity {

    public static final String TAG = "SplashScreen";

    private final LanguageChangeFacade languageChangeFacade = new LanguageChangeFacade();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Move this where it will be called on crash>restart
        // apply default language
        LanguageUtils.withBaseContext(getBaseContext()).commitChanges();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startNextActivity();
    }

    /*
     * start appropriate activity
     */
    private void startNextActivity(){
        try {
            // check if user is running this application for the first time
            if (Util.isFirstRun(this, Util.RunningMode.FIRST_TIME_INSTALL)) {
                // ask language preference
                showLanguagePickerDialog();
            } else {
                if (!PhoneVerificationUtils.isVerified(this)) {
                    startActivity(new Intent(this, UserDetailsActivity.class));
                    finish();
                } else {
                    // go home
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }            }
        } catch (Exception e) {
            // ignore
        }
    }

    private void showLanguagePickerDialog() {
        SingleItemSelectionDialog itemSelectionDialog = SingleItemSelectionDialog.Builder.withContext(this)
            .addItems(getResources().getStringArray(R.array.languages))
            .setActionSelectText(R.string.action_select)
            .setActionCancelText(R.string.text_cancel)
            .setOnAcceptSelection(languageChangeFacade)
            .setOnActionListener(languageChangeFacade)
            .setOnCancelListener(languageChangeFacade)
            .setOnDismissListener(languageChangeFacade)
            .setTitle(R.string.title_select_default_language)
            .build();
        itemSelectionDialog.open();
    }

    private class LanguageChangeFacade implements
            SingleItemSelectionDialog.OnAcceptSelection,
            DialogInterface.OnClickListener, DialogInterface.OnCancelListener,
            DialogInterface.OnDismissListener {

        private String mSelectedLanguage;

        private void startIntroActivity() {
            if (!TextUtils.isEmpty(mSelectedLanguage)) {

                LanguageUtils languageUtils = LanguageUtils.withBaseContext(getBaseContext());
                String[] languages = getResources().getStringArray(R.array.languages);

                for (String lang: languages) {
                    if (lang.equals(mSelectedLanguage)) {
                        languageUtils.setDefaultLanguage(lang);
                        break;
                    }
                }
            }

            // finish the activity. No need to be able to get back to the splash screen activity
            startActivity(new Intent(SplashScreenActivity.this, AppIntroActivity.class));
            finish();
        }

        @Override
        public void onItemSelected(String item, int position) {
            this.mSelectedLanguage = item;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            startIntroActivity();
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            startIntroActivity();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            startIntroActivity();
        }
    }
}
