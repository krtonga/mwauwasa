package com.github.codetanzania.util;

import android.content.res.Resources;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;

import java.text.DecimalFormat;

import tz.co.codetanzania.R;

/**
 * This is used for Map functions that can be used throughout the app.
 */

public class MapUtils {
    private static LatLngBounds companyBounds;

    public static LatLngBounds getCompanyBounds(Resources resources) {
        if (companyBounds == null) {
            Float neLat = Float.parseFloat(resources.getString(R.string.map_ne_lat));
            Float neLng = Float.parseFloat(resources.getString(R.string.map_ne_lng));
            Float swLat = Float.parseFloat(resources.getString(R.string.map_sw_lat));
            Float swLng = Float.parseFloat(resources.getString(R.string.map_sw_lng));

            companyBounds = new LatLngBounds.Builder()
                    .include(new LatLng(neLat, neLng)) // Northeast
                    .include(new LatLng(swLat, swLng))  // Southwest
                    .build();
        }

        return companyBounds;
    }

    private static DecimalFormat coordinateFormat = new DecimalFormat("#.0000");

    public static String formatCoordinateString(Resources resources, LatLng location) {
        if (location == null) {
            return resources.getString(R.string.default_address_display);
        }
        return String.format(resources.getString(R.string.coordinate_display),
                    coordinateFormat.format(location.getLatitude()),
                    coordinateFormat.format(location.getLongitude()));
    }
}
