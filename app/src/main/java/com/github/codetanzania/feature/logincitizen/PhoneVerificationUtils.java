package com.github.codetanzania.feature.logincitizen;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.github.codetanzania.Constants;
import com.github.codetanzania.util.Util;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tz.co.codetanzania.R;

public class PhoneVerificationUtils {
    private static final String TAG = "PhoneVerificationUtils";

    public static String parseSms(Resources resources, @NonNull Bundle smsBundle) {

        Object[] PDUs = (Object[]) smsBundle.get("pdus");

        if (Util.isNull(PDUs)) {
            return null;
        }

        SmsMessage[] messages = new SmsMessage[PDUs.length];

        for (int i = 0; i < messages.length; i++) {
            messages[i]= SmsMessage.createFromPdu((byte[]) PDUs[i]);
            String address = messages[i].getOriginatingAddress();
            String message = messages[i].getDisplayMessageBody();

            Log.d(TAG, "Sender: " + address);
            Log.d(TAG, "Message: " + message);

            if (address.toLowerCase().equals(
                    resources.getString(R.string.app_name).toLowerCase())) {
                return message;
            }
        }

        return null;
    }

    private static String parseVerificationCode(String sms) {
        Pattern pattern = Pattern.compile("\\d{4}");
        Matcher matcher = pattern.matcher(sms);
        String verificationCode = "";
        while (matcher.find()) {
            verificationCode += sms.substring(matcher.start(), matcher.end());
        }
        return verificationCode;
    }

    public static boolean isValidVerificationCode(
            @NonNull Context ctx, @NonNull String code) {
        String confirmationCode = readVerificationCode(ctx);

        if (TextUtils.isEmpty(confirmationCode)) {
            return false;
        }

        return confirmationCode.equals(code);
    }

    public static void setVerified(Context ctx, boolean flag) {
        SharedPreferences prefs = ctx
                .getSharedPreferences(Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(Constants.Company.USER_VERIFIED, flag)
            .apply();
    }

    public static boolean isVerified(Context ctx) {
        SharedPreferences prefs = ctx
                .getSharedPreferences(Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(Constants.Company.USER_VERIFIED, false);
    }

    public static String readVerificationCode(Context ctx) {
        SharedPreferences mPrefs = ctx.getSharedPreferences(
                Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        return mPrefs.getString(Constants.MajiFix.SMS_VERIFICATION_CODE, null);
    }

    public static String getRandomVerificationCode() {
        return String.format(Locale.getDefault(), "%d", (int)(Math.random() * 9000) + 1000);
    }

    public static void storeVerificationCode(Context ctx, String code) {
        SharedPreferences mPrefs = ctx.getSharedPreferences(
                Constants.KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        mPrefs.edit().putString(
                Constants.MajiFix.SMS_VERIFICATION_CODE, code).apply();
    }

    public static boolean isValidPhoneNumber(
            @NonNull String phoneNumber, @NonNull String iso3CountryCode) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumberInstance;
        try {
            phoneNumberInstance = phoneNumberUtil.parse(phoneNumber, iso3CountryCode);
        } catch (NumberParseException npe) {
            Log.e(TAG, "\n\nAn exception was:" + npe.getMessage() + "\n\n");
            return false;
        }
        return phoneNumberUtil.isValidNumber(phoneNumberInstance);
    }
}
