package com.github.codetanzania.feature.report;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.codetanzania.Constants;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import tz.co.codetanzania.R;

public final class FetchAddressIntentService extends IntentService {

    // used by logcat
    public static final String TAG = "FetchAddressService";

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";

    protected ResultReceiver mReceiver;

    public FetchAddressIntentService() {
        this("FetchAddressIntentService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FetchAddressIntentService(String name) {
        super(name);
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        String errorMessage = "";

        // Get the location passed to this service through an extra.
        LatLng location = intent.getParcelableExtra(
                LOCATION_DATA_EXTRA);

        if (location == null) {
            return;
        }

        // Get Receiver passed through extra
        mReceiver = intent.getParcelableExtra(RECEIVER);

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.location_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            List<String> addressFragments = new ArrayList<>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            if (!addressFragments.isEmpty()) {
                Log.i(TAG, getString(R.string.address_found) + Arrays.toString(addressFragments.toArray()));
                deliverResultToReceiver(SUCCESS_RESULT, addressFragments.get(0));
            } else {
                Log.e(TAG, "No address found.");
                deliverResultToReceiver(FAILURE_RESULT, getString(R.string.no_address_found));
            }
        }
    }

    public static class AddressResultReceiver extends ResultReceiver {
        private Receiver mReciever;

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        public void setmReciever(Receiver reciever) {
            this.mReciever = reciever;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                if (mReciever != null) {
                    mReciever.onReceiveAddress(resultData.getString(Constants.RESULT_DATA_KEY));
                }
            }

        }
    }

    interface Receiver {
        void onReceiveAddress(String address);
    }
}
