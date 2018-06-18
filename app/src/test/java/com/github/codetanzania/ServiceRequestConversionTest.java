package com.github.codetanzania;

import android.os.Parcel;

import com.github.codetanzania.core.model.ServiceRequest;
import com.github.codetanzania.util.ServiceRequestsUtil;
import com.github.codetanzania.utils.Mocks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;

import tz.co.codetanzania.BuildConfig;

/**
 * This is used to ensure that json is properly converted to app objects,
 * and that app objects are properly parsed between activities.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, shadows = {})
public class ServiceRequestConversionTest {

    @Test
    public void serviceRequestUtil_fromJson() throws IOException {
        ServiceRequest request =
                ServiceRequestsUtil.fromJson(RuntimeEnvironment.application, Mocks.validRequest).getRequests().get(0);
        Mocks.isSame(request);
    }

    @Test
    public void serviceRequest_isParcelable() throws IOException{
        ServiceRequest request =
                ServiceRequestsUtil.fromJson(RuntimeEnvironment.application, Mocks.validRequest).getRequests().get(0);

        Parcel parcel = Parcel.obtain();
        request.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        ServiceRequest fromParcel = ServiceRequest.CREATOR.createFromParcel(parcel);
        Mocks.isSame(fromParcel);
    }
}
