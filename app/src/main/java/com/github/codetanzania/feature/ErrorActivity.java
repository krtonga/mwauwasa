package com.github.codetanzania.feature;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.codetanzania.feature.list.ErrorFragment;

import tz.co.codetanzania.R;

public class ErrorActivity extends AppCompatActivity
        implements ErrorFragment.OnReloadClickListener{

    public static final String PONG_PARAMS = "error.pong";
    private static Class<?> pongClass;
    private Bundle mRetBundle;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_err);
        mRetBundle = getIntent().getBundleExtra(PONG_PARAMS);
    }

    @Override
    public void onReloadClicked() {
//        Intent intent = new Intent(this, pongClass);
//        if (mRetBundle != null) {
//            intent.putExtras(mRetBundle);
//        }
//        startActivity(intent);
        finish();
    }

    public static <T> Class<ErrorActivity> memoizeClass(Class<T> tClass) {
        pongClass = tClass;
        return ErrorActivity.class;
    }
}
