package com.github.codetanzania.shadows;

import android.content.Context;

import com.github.codetanzania.core.model.Reporter;
import com.github.codetanzania.util.Util;
import com.github.codetanzania.utils.Mocks;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

/**
 * This is used to mock certain methods from the Util class for testing.
 */

@Implements(Util.class)
public class ShadowUtils {

    @Implementation
    public static Reporter getCurrentReporter(Context mContext) {
        return Mocks.getMockReporter();
    }

    @Implementation
    public static void storeCurrentReporter(Context mContext, Reporter reporter) {
        Mocks.mockReporter = reporter;
    }


        @Implementation
    public static String getAuthToken(Context mContext) {
        return "mockToken";
    }
}
