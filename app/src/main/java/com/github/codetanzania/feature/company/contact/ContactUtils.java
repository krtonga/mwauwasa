package com.github.codetanzania.feature.company.contact;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.Locale;

import tz.co.codetanzania.R;

/**
 * This is used for Utils used for the COMPANY specific fragments.
 */

public class ContactUtils {
    public static final String TELEPHONE_DATA = "data.telephone";
    public static final String EMAIL_DATA = "data.email";
    public static final String URI_DATA = "data.uri";

    public static void callCallCenter(Activity activity) {
        initiateCall(activity, null);
    }

    public static void initiateCall(Activity activity, Bundle data) {
        Uri phoneUri;
        String phoneNumber = activity.getString(R.string.call_center_number);

        if (data == null) {
            phoneUri = Uri.parse("tel:" + phoneNumber);
        } else {
            phoneUri = data.getParcelable(TELEPHONE_DATA);
        }

        String title       = String.format(Locale.getDefault(),
                activity.getString(R.string.text_call_company), activity.getString(R.string.app_name), phoneUri);
        Intent intent = new Intent(Intent.ACTION_DIAL, phoneUri);
        dispatchIntentChooser(activity, intent, title);
    }

    public static void initiateBrowser(Activity activity, Bundle data) {
        Uri webUri = data.getParcelable(URI_DATA);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, webUri);
        dispatchIntentChooser(activity, browserIntent, activity.getString(R.string.action_visit_web));
    }

    public static void initiateGoogleMap(Activity activity, Bundle data) {
        Uri geoDataUri = data.getParcelable(URI_DATA);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoDataUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        dispatchIntentChooser(activity, mapIntent, activity.getString(R.string.action_get_directions));
    }

    public static void initiateEmail(Activity activity, Bundle data) {
        String email = data.getString(EMAIL_DATA);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.email_default_subject));
        intent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.email_default_body));
        dispatchIntentChooser(activity, intent, activity.getString(R.string.action_get_email));
    }

    private static void dispatchIntentChooser(Activity activity, Intent targetIntent, String title) {
        if (targetIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create intent chooser
            Intent chooser = Intent.createChooser(targetIntent, title);
            activity.startActivity(chooser);
        }
    }
}
