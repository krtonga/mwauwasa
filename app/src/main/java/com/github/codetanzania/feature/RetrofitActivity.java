package com.github.codetanzania.feature;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.codetanzania.feature.views.ObstructiveProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Base activity for activities that need to fetch data using Retrofit.
 */

public abstract class RetrofitActivity<T> extends AppCompatActivity implements Callback<T> {
    private Call<T> mHttpCall;

    /* Stores data from server. Use activity callbacks to retain
     * this data so that we do not need to query it again when activity
     * onResume() is invoked e.g when screen orientation changes. */
    protected T mData;

    /*
     * The dialog to show when data is being fetched from the network
     */
    private ObstructiveProgressDialog mObstructiveProgressDialog;


    /*
     * Method invoked to initialize mHttpCall.
     * Return null if you want to skip loading data using retrofit
     */
    protected @Nullable
    abstract Call<T> initializeCall();

    /*
     * Method to assign data from Http response.
     */
    protected abstract  T getData(Response<T> response);

    @Override
    protected void onResume() {
        super.onResume();

        // If no data, make call
        onResumeGetData();
    }

    protected void onResumeGetData() {
        refreshData(false, true);
    }

    protected void onProcessResponse(T data, int httpStatusCode) {}

    @Override
    protected void onPause() {
        cancelPendingCall();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        cancelPendingCall();
        if (mObstructiveProgressDialog != null) {
            mObstructiveProgressDialog.dispose();
        }
        super.onDestroy();
    }

    protected void refreshData(boolean forceReload, boolean blockUi) {
        if (!forceReload && mData != null
                && (mHttpCall != null && !mHttpCall.isCanceled())) {
            return;
        }
        System.out.println("Refresh starting...");
        mHttpCall = initializeCall();
        if (mHttpCall != null) {
            System.out.println("Refresh enqueued...");
            mHttpCall.enqueue(this);
            // show dialog
            if (blockUi) {
                mObstructiveProgressDialog = new ObstructiveProgressDialog(this);
                mObstructiveProgressDialog.show();
            }
        }
    }

    private void cancelPendingCall() {
        if (mHttpCall != null
                && mHttpCall.isExecuted()
                && !mHttpCall.isCanceled()) {
            mHttpCall.cancel();
        }
    }

    protected void showProgressDialog() {
        if (this.mObstructiveProgressDialog == null) {
            this.mObstructiveProgressDialog = new ObstructiveProgressDialog(this);
        }
        this.mObstructiveProgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (this.mObstructiveProgressDialog != null) {
            mObstructiveProgressDialog.dismiss();
        }
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful() && !isFinishing()) {
            mData = getData(response);
            onProcessResponse(mData, response.code());
        }
        // dismiss any dialogs
        if (mObstructiveProgressDialog != null && mObstructiveProgressDialog.isShowing()) {
            mObstructiveProgressDialog.dismiss();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        // TODO add error callback
        if (mObstructiveProgressDialog != null) {
            mObstructiveProgressDialog.dismiss();
        }
        mHttpCall = null;
    }
}
