package com.github.codetanzania.feature;

import android.support.v4.app.Fragment;

import com.github.codetanzania.feature.views.ObstructiveProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is used to make fragment retrofit calls.
 */

public abstract class RetrofitFragment<T> extends Fragment implements Callback<T> {
    protected Call<T> mHttpCall;
    protected ObstructiveProgressDialog mObstructiveProgressDialog;

    public abstract void onSuccess(T result);

    @Override
    public void onPause() {
        cancelPendingCall(mHttpCall);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        cancelPendingCall(mHttpCall);
        if (mObstructiveProgressDialog != null) {
            mObstructiveProgressDialog.dispose();
        }
        super.onDestroy();
    }

    public void triggerServerCall(Call<T> httpCall) {
        mHttpCall = httpCall;
        if (mHttpCall != null) {
            mHttpCall.enqueue(this);
            mObstructiveProgressDialog = new ObstructiveProgressDialog(getActivity());
            mObstructiveProgressDialog.show();
        }
    }

    protected void cancelPendingCall(Call httpCall) {
        if (httpCall != null
                && httpCall.isExecuted()
                && !httpCall.isCanceled()) {
            httpCall.cancel();
        }
    }

    protected void hideProgressDialog() {
        if (this.mObstructiveProgressDialog != null) {
            mObstructiveProgressDialog.dismiss();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        System.out.println("Call failed: "+call+"\n"+t);
        hideProgressDialog();
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!getActivity().isFinishing()) {
            if (response.isSuccessful()) {
                onSuccess(response.body());
            } else {
                onFailure(call, new Exception("Network error"));
            }
        }
        hideProgressDialog();
    }
}
