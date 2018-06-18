package com.github.codetanzania.feature.logincitizen;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.codetanzania.core.api.Open311Api;
import com.github.codetanzania.feature.home.MainActivity;
import com.github.codetanzania.open311.android.library.models.Reporter;
import com.github.codetanzania.util.LocalTimeUtils;
import com.github.codetanzania.util.SmsUtils;
import com.github.codetanzania.util.Util;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tz.co.codetanzania.BuildConfig;
import tz.co.codetanzania.R;

public class OTPVerificationActivity extends AppCompatActivity
    implements Callback<ResponseBody>,
        DialogInterface.OnCancelListener,
        DialogInterface.OnClickListener,
        SmsVerificationBroadcastReceiver.OnVerificationResult,
        View.OnClickListener, TextWatcher {

    private static final String TAG = "OTPVerification";

    private static final int SMS_PERMISSION_CODE = 0;

    // reference to the views
    private TextView tvCountDown;
    private TextView tvOtpTitle;
    private TextInputEditText tilVerificationCode;
    private Button btnRetry;
    private Button btnCancel;

    // Constants used by the timer
    private static long VERIFICATION_TIMEOUT = 60000;
    private static final int UPDATE_INTERVAL = 1000;

    private long mCurrentTick = VERIFICATION_TIMEOUT;

    /* broadcast receiver for the SMSs */
    private SmsVerificationBroadcastReceiver mSmsReceiver;
    private String mVerificationCode;

    private CountDownTimer mCountDownTimer;

    /* CountDown timer to keep track of the number of second remains until the request is cancelled */
    private CountDownTimer getCountDownTimer() {
        return new CountDownTimer(mCurrentTick, UPDATE_INTERVAL) {
            @Override
            public void onTick(long l) {
                tvCountDown.setText(LocalTimeUtils
                        .formatCountDown(l, LocalTimeUtils.FMT_MM_SS));
                mCurrentTick = l;
            }

            @Override
            public void onFinish() {
                if (!PhoneVerificationUtils.isVerified(
                        OTPVerificationActivity.this)) {
                    showOptions();
                }
            }
        };
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otp_verification);

        initViews();

        Log.d(TAG, "VISITED");

        if (mSmsReceiver == null) {
            mSmsReceiver = new SmsVerificationBroadcastReceiver();
            registerReceiver(mSmsReceiver,
                    new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        }

        mSmsReceiver.setOnVerificationResult(this);

        bootstrapVerification();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: " + mCurrentTick);
        // Log.d(TAG, "onResume: ");

        mCountDownTimer = getCountDownTimer();
        mCountDownTimer.start();
    }

    @Override public void onPause() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        super.onPause();
    }

    @Override public void onDestroy() {
        if (mSmsReceiver != null) {
            unregisterReceiver(mSmsReceiver);
            mSmsReceiver = null;
        }

        super.onDestroy();
    }

    private void initViews() {
        tvOtpTitle = (TextView) findViewById(R.id.tv_OtpTitle);
        tvCountDown = (TextView) findViewById(R.id.tv_OtpCountDown);
        tilVerificationCode = (TextInputEditText) findViewById(R.id.til_VerificationCode);
        btnRetry = (Button) findViewById(R.id.btn_OtpRetry);
        btnCancel = (Button) findViewById(R.id.btn_ChangeNumber);
        btnRetry.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        tilVerificationCode.addTextChangedListener(this);
    }

    private void showOptions() {
        btnRetry.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
    }

    private void bootstrapVerification() {
        // generate and store random code
        mVerificationCode = PhoneVerificationUtils.getRandomVerificationCode();
        PhoneVerificationUtils.storeVerificationCode(this, mVerificationCode);
        // initialize broadcast receiver
        if (hasReadSmsPermission()) {
            // send verification code using SMS transporter [presumably, EGA]
            SmsUtils.sendVerificationCode(getApplicationContext(), mVerificationCode, this);
        } else {
            showPermitClarificationDialog();
        }
    }

    private boolean hasReadSmsPermission() {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
               ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS
        }, SMS_PERMISSION_CODE);
    }

    private void showMessage(String msg) {
        tilVerificationCode.setText(msg);
        tilVerificationCode.setTextColor(Color.parseColor("#262626"));
    }

    private void showPermitClarificationDialog() {
        int msgId = R.string.msg_reason_to_read_sms;
        int txtGrant = R.string.text_grant_read_sms_permission;
        int txtDeny = R.string.text_deny_read_sms_permission;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msgId)
                .setPositiveButton(txtGrant, this)
                .setNegativeButton(txtDeny, this)
                .create().show();
        tvOtpTitle.setText(R.string.title_otp_requesting_permission);
        tilVerificationCode.setVisibility(View.INVISIBLE);
    }

    private void exitApplication() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onRequestPermissionsResult(
        int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // send verification code using SMS transporter [presumably, EGA]
                    SmsUtils.sendVerificationCode(getApplicationContext(), mVerificationCode, this);
                } else {
                    // show dialog message before quiting
                    exitApplication();
                }
            }
        }
    }

    @Override
    public void onResponse(
            @NonNull Call<ResponseBody> call,
            @NonNull Response<ResponseBody> response) {
//        if (response.isSuccessful()) {
//            mCountDownTimer.start();
//        }
    }

    @Override
    public void onFailure(
            Call<ResponseBody> call,
            Throwable t) {
        Toast.makeText(this,
            R.string.error_delivering_sms, Toast.LENGTH_SHORT).show();
        showOptions();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // there's no need now!
        exitApplication();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            requestReadSmsPermission();
            dialog.dismiss();
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            exitApplication();
        }
    }

    @Override
    public void onVerificationSuccessful(String message, boolean userAction) {
        PhoneVerificationUtils.setVerified(this, true);
        if (!userAction) {
            showMessage(message);
        }
        // start another activity and finish this.
        // As a result, this will call our onPause, causing the activity
        // to tear down, which includes stopping the timer and
        // un-registering the broadcast receiver.
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onVerificationFailed() {
        // Show option buttons [retry|quit]
        showOptions();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_OtpRetry:
                recreate();
                break;
            case R.id.btn_ChangeNumber:
                startActivity(new Intent(this, UserDetailsActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(
            CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(
            CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        // manual verification
        if (!TextUtils.isEmpty(s)) {
            if (s.toString().equals(mVerificationCode)) {
                tilVerificationCode.setError(null);
                onVerificationSuccessful(mVerificationCode, true);
            } else {
                if (s.length() >= 4) {
                    tilVerificationCode.setError(getString(R.string.text_invalid_verification_code));
                } else {
                    tilVerificationCode.setError(null);
                }
            }
        }
    }
}
