package com.github.codetanzania.util;

import android.content.Context;
import android.view.View;

import com.github.codetanzania.Constants;
import com.github.codetanzania.core.api.Open311Api;
import com.github.codetanzania.open311.android.library.models.Reporter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import tz.co.codetanzania.BuildConfig;
import tz.co.codetanzania.R;

public class SmsUtils {
    public static void sendVerificationCode(Context context,
                                            String verificationCode,
                                            Callback<ResponseBody> callback) {
        Reporter reporter = Util.getCurrentReporter(context);
        assert reporter != null;

        // Use Majifix SMS service
        Map<String, String> smsBody = new HashMap<>();
        smsBody.put("body", verificationCode);
        smsBody.put("type", "SMS");
        smsBody.put("to", reporter.getPhone());

        String authToken = BuildConfig.MAJIFIX_TOKEN;

        new Open311Api.ServiceBuilder(context)
                .build(Open311Api.SendSMSEndpoint.class)
                .sendSms(authToken, smsBody)
                .enqueue(callback);
    }
}
