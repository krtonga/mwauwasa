package com.github.codetanzania.util;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.github.codetanzania.Constants;
import com.github.codetanzania.open311.android.library.models.Problem;
import com.github.codetanzania.feature.issuedetails.IssueProgressActivity;
import com.github.codetanzania.feature.initialization.SplashScreenActivity;
import com.github.codetanzania.open311.android.library.models.Reporter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import tz.co.codetanzania.BuildConfig;
import tz.co.codetanzania.R;

public class Util {

    public static final String TAG = "Util";

    public enum RunningMode {
        FIRST_TIME_INSTALL,
        FIRST_TIME_UPGRADE
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isFirstRun(Context mContext, RunningMode mRunningMode) throws Exception {

        int currentVersionCode, savedVersionCode;

        try {
            currentVersionCode = mContext.getPackageManager()
                    .getPackageInfo(mContext.getPackageName(), 0)
                    .versionCode;

        } catch(android.content.pm.PackageManager.NameNotFoundException e) {
            Log.e(TAG, String.format("An exception is %s", e.getMessage()));
            throw new Exception(
                    String.format("Package name not found. Original exception was: %s ", e.getMessage()));
        }

        SharedPreferences sharedPrefs = mContext
                .getSharedPreferences(Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE);

        savedVersionCode = sharedPrefs.getInt(Constants.APP_VERSION_CODE, -1);

        boolean firstTimeRun = savedVersionCode == -1;
        boolean upgradeRun   = savedVersionCode <  currentVersionCode;

        if (firstTimeRun || upgradeRun) {
            sharedPrefs.edit().putInt(
                    Constants.APP_VERSION_CODE, currentVersionCode).apply();
        }

        if (mRunningMode == RunningMode.FIRST_TIME_INSTALL) {
            return firstTimeRun;
        } else {
            return mRunningMode == RunningMode.FIRST_TIME_UPGRADE && upgradeRun;
        }
    }

    public static Reporter getCurrentReporter(Context mContext) {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(
                Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        String phone = sharedPrefs.getString(Constants.MajiFix.REPORTER_PHONE, null);

        // logical to use phone number which we verify through OTP
        if (phone == null) {
            return null;
        }

        String email = sharedPrefs.getString(Constants.MajiFix.REPORTER_EMAIL, null);
        String account = sharedPrefs.getString(Constants.MajiFix.REPORTER_WATER_ACCOUNT, null);
        String fullName = sharedPrefs.getString(Constants.MajiFix.REPORTER_NAME, null);

        Reporter reporter = new Reporter();
        reporter.setAccount(account);
        reporter.setPhone(phone);
        reporter.setName(fullName);
        reporter.setEmail(email);

        return reporter;
    }

    public static void loginUser(Context context, String accountNumber, boolean isLoggedIn) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPrefs.edit()
                .putString(Constants.MajiFix.REPORTER_WATER_ACCOUNT, accountNumber)
                .putBoolean(Constants.Company.COMPANY_CUSTOMER_SIGNED_IN, isLoggedIn)
                .apply();
    }

    public static void writeUserPassword(Context context, String password) {
        context.getSharedPreferences(Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE)
                .edit().putString(Constants.Company.COMPANY_CUSTOMER_PASSWORD, password).apply();
    }

    public static void requireCompanyLogin(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPrefs.edit()
                .putBoolean(Constants.Company.COMPANY_CUSTOMER_SIGNED_IN, false)
                .apply();
    }

    public static String readUserPassword(Context context) {
        return context.getSharedPreferences(Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE)
                .getString(Constants.Company.COMPANY_CUSTOMER_PASSWORD, null);
    }

    public static void logoutCompany(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPrefs.edit()
                .putString(Constants.MajiFix.REPORTER_WATER_ACCOUNT, null)
                .putBoolean(Constants.Company.COMPANY_CUSTOMER_SIGNED_IN, false)
                .apply();
    }

    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE);

        return sharedPrefs.getBoolean(Constants.Company.COMPANY_CUSTOMER_SIGNED_IN, false);
    }

    public static void storeCurrentReporter(Context context, Reporter reporter) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPrefs.edit()
                .putString(Constants.MajiFix.REPORTER_NAME, reporter.getName())
                .putString(Constants.MajiFix.REPORTER_PHONE, reporter.getPhone())
                .putString(Constants.MajiFix.REPORTER_EMAIL, reporter.getEmail())
                .putString(Constants.MajiFix.REPORTER_WATER_ACCOUNT, reporter.getAccount())
                .apply();
    }

    public static String getAuthToken() {
        // TODO obfuscate token
        return "Bearer "+ BuildConfig.MAJIFIX_TOKEN;
    }

    public static void storeUserId(Context mContext, String mUserId) {
        SharedPreferences mPrefs = mContext.getSharedPreferences(Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        mPrefs.edit()
                .putString(Constants.MajiFix.CURRENT_USER_ID, mUserId)
                .apply();
    }

    public static String parseJWTToken(String input) throws JSONException {
        JSONObject jsObj = new JSONObject(input);
        return jsObj.getString("token");
    }

    public static String parseUserId(String input) throws JSONException {
        JSONObject jsObj = new JSONObject(input);
        return jsObj.getJSONObject("party").getString("_id");
    }

    public static boolean isGPSOn(Context context) {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void resetPreferences(Context mContext) {
        mContext.getSharedPreferences(
                Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

    public static void addDateToParcel(Parcel parcel, Date date) {
        parcel.writeLong(date == null ? 0 : date.getTime());
    }

    public static Date extractDateFromParcel(Parcel parcel) {
        long extractedDate = parcel.readLong();
        return extractedDate == 0 ? null : new Date(extractedDate);
    }

    public static void hideSoftInputMethod(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSoftInputMethod(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            return;
        }
        final InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static String timeElapse(Date d1, Date d2, Context context) {
        long duration   = Math.abs(d1.getTime() - d2.getTime());
        long diffInDays = TimeUnit.DAYS.convert(duration, TimeUnit.MILLISECONDS);
        long remHours   = duration - (diffInDays * 24 * 60 * 60 * 1000);
        long diffInHours = TimeUnit.HOURS.convert(remHours, TimeUnit.MILLISECONDS);

        SpannableStringBuilder builder = new SpannableStringBuilder();

        if (diffInDays != 0) {
            builder.append(String.format(Locale.getDefault(), "%d", diffInDays)).append(" days");
        }
        builder.append(String.format(Locale.getDefault(), " %d", diffInHours)).append(" hours");

        return builder.toString();
    }

    /* DRY Principle. This logic is supposedly invoked by many activity to Preview Issue details */
    public static void startPreviewIssueActivity(Activity fromActivity, Problem request) {
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.MajiFix.TICKET, request);
        Intent activityIntent = new Intent(fromActivity, IssueProgressActivity.class);
        activityIntent.putExtras(extras);
        fromActivity.startActivity(activityIntent);
    }

    /* logout method */
    public static void confirmExitApp(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(R.string.text_confirm_logout)
                .setPositiveButton(R.string.action_logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetPreferences(activity);
                        Intent intent = new Intent(activity, SplashScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                        activity.finishAffinity();
                    }
                })
                .setNegativeButton(R.string.action_stay, null)
                .show();
    }
}
