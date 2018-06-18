package com.github.codetanzania;

/*import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import com.github.codetanzania.core.model.Reporter;
import com.github.codetanzania.shadows.ShadowUtils;
import com.github.codetanzania.ui.activity.EditUserProfileActivity;
import com.github.codetanzania.feature.settings.SettingsActivity;
import com.github.codetanzania.utils.MocksSrvcRq;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowIntent;

import tz.co.codetanzania.BuildConfig;
import tz.co.codetanzania.R;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

*//**
 * This unit test is used to test the settings and profile screen
 * shown when a user clicks Settings on the Home page.
 *//*

/*@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, shadows = {ShadowUtils.class})
public class SettingsActivityTest {
    private SettingsActivity mActivity;

    private FloatingActionButton fab;
    private TextView tvUsername;
    private TextView tvPhoneNumber;
//    private TextView tvEmail;
//    private TextView tvLocation;
//    private TextView tvMeterNumber;

    @Before
    public void setup() {
        mActivity = Robolectric.buildActivity(
                SettingsActivity.class).create().start().resume().get();

        fab = (FloatingActionButton) mActivity.findViewById(R.id.fab_EditProfile);
        tvUsername = (TextView) mActivity.findViewById(R.id.tv_UserName);
        tvPhoneNumber = (TextView) mActivity.findViewById(R.id.tv_UserPhone);
//        tvEmail = (TextView) findViewById(R.id.tv_UserEmail);
//        tvLocation = (TextView) findViewById(R.id.tv_UserLocation);
//        tvMeterNumber = (TextView) findViewById(R.id.tv_UserMeterNumber);
    }

    @Test
    public void settingsActivity_starts() {
        assertNotNull(fab);
        assertNotNull(mActivity.getSupportActionBar());
        assertNotNull(tvUsername);
        assertNotNull(tvPhoneNumber);
    }

    @Test
    public void settingsActivity_showsReporter() {
        Reporter reporter = ShadowUtils.getCurrentReporter(RuntimeEnvironment.application);
        assertEquals("Name should be correct",
                reporter.name, tvUsername.getText());
        assertEquals("Phone number should be correct",
                reporter.phone, tvPhoneNumber.getText());
    }

    @Test
    public void settingsActivity_shouldAllowUserToEditProfile() {
        fab.performClick();

        Intent startedIntent = shadowOf(mActivity).getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertEquals("Edit Profile screen should start on fab click.",
                EditSettingsActivity.class, shadowIntent.getIntentClass());
    }

//    private void findUserInputFields() {
//        etUserName = (EditText) mActivity.findViewById(R.id.et_userName);
//        etZipCode = (EditText) mActivity.findViewById(R.id.et_AreaCode);
//        etPhoneNumber = (EditText) mActivity.findViewById(R.id.et_phoneNumber);
//        etUserEmail = (EditText) mActivity.findViewById(R.id.et_UserEmail);
//        etAccountNumber = (EditText) mActivity.findViewById(R.id.et_AccountNumber);
//    }

    private void findUserInfoFields() {

    }

}*/
