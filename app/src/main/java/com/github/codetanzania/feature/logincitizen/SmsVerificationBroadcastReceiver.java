package com.github.codetanzania.feature.logincitizen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import static com.github.codetanzania.feature.logincitizen.PhoneVerificationUtils.isValidVerificationCode;
import static com.github.codetanzania.feature.logincitizen.PhoneVerificationUtils.parseSms;

/* Broadcast receiver. listen for the incoming verification code */
public class SmsVerificationBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = "VerificationRoutine";

    private OnVerificationResult mOnVerificationResult;

    public void setOnVerificationResult(
            OnVerificationResult onVerificationResult) {
        this.mOnVerificationResult = onVerificationResult;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (mOnVerificationResult != null) {

            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                String verificationSms =
                        parseSms(context.getResources(), bundle);
                Log.d(TAG, "\n\nReceived Message:\n\n" + verificationSms);
                if (!TextUtils.isEmpty(verificationSms) && isValidVerificationCode(
                        context, verificationSms)) {
                    // update listener
                    this.mOnVerificationResult.onVerificationSuccessful(verificationSms, false);
                } else {
                    this.mOnVerificationResult.onVerificationFailed();
                }
            }

        }
    }

    public interface OnVerificationResult {
        void onVerificationSuccessful(String message, boolean userAction);
        void onVerificationFailed();
    }
}