package com.github.codetanzania.feature.issuedetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.github.codetanzania.feature.MapboxBaseFragment;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import tz.co.codetanzania.R;

public class StaticMapFragment extends MapboxBaseFragment {

    public static final String KEY_LONGITUDE = StaticMapFragment.class.getSimpleName() + "/longitude";
    public static final String KEY_LATITUDE  = StaticMapFragment.class.getSimpleName() + "/latitude";

    private float longitude;
    private float latitude;

    public static StaticMapFragment getNewInstance(float latitude, float longitude) {
        StaticMapFragment frag = new StaticMapFragment();
        Bundle args = new Bundle();
        args.putFloat(StaticMapFragment.KEY_LATITUDE,  latitude);
        args.putFloat(StaticMapFragment.KEY_LONGITUDE, longitude);
        frag.setArguments(args);
        return frag;
    }

    View.OnClickListener mClickListener;

    public void setClickListener(final View.OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // read coordinates from the restored state or from the arguments
        Bundle bundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        longitude = bundle.getFloat(KEY_LONGITUDE);
        latitude  = bundle.getFloat(KEY_LATITUDE);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        outState.putFloat(KEY_LATITUDE, latitude);
        outState.putFloat(KEY_LONGITUDE, longitude);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getFragLayoutId() {
        return R.layout.card_view_map;
    }


    @Override
    protected int getMapViewId() {
        return R.id.mapView;
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        super.onMapReady(mapboxMap);

        mCurrentLocation = new LatLng(latitude, longitude);
        updateCamera();
        addMarker(mCurrentLocation);

        if (mClickListener != null) {
            mMapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng point) {
                    mClickListener.onClick(mMapView);
                }
            });
        }
    }
}
