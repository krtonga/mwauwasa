//package com.github.codetanzania;
//
//import android.support.v4.app.FragmentActivity;
//
//import com.github.codetanzania.shadows.ShadowUtils;
//import com.github.codetanzania.feature.settings.EditSettingsActivity;
//import com.github.codetanzania.utils.Mocks;
//import com.github.codetanzania.utils.ProfileFragmentTest;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.Robolectric;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.annotation.Config;
//
//import tz.co.codetanzania.BuildConfig;
//import tz.co.codetanzania.R;
//
//import static junit.framework.Assert.assertEquals;
//import static junit.framework.Assert.assertNull;
//import static junit.framework.Assert.assertTrue;
//import static org.junit.Assert.assertNotNull;
//import static org.robolectric.Shadows.shadowOf;
//
///**
// * This unit test is used to test the edit profile screen
// * shown when a user clicks Edit in Settings. Validation tests of user
// * inputs are contained in parent ProfileFragmentTest class.
// */
//
//@RunWith(RobolectricTestRunner.class)
//@Config(constants = BuildConfig.class, shadows = {ShadowUtils.class})
//public class EditProfileActivityTest extends ProfileFragmentTest {
//
//    @Override
//    public void setup() {
//        super.setup();
//
//        assertEquals("Username should be shown on start",
//                Mocks.getMockReporter().name, mUsername.getEditText().getText().toString());
//        assertEquals("Area code should be shown on start",
//                Mocks.getMockReporter().phone.substring(0, 3), mAreaCode.getEditText().getText().toString());
//        assertEquals("Phone number should be shown on start",
//                Mocks.getMockReporter().phone.substring(3), mPhoneNumber.getEditText().getText().toString());
//    }
//
//    @Override
//    public FragmentActivity setActivity() {
//        return Robolectric.buildActivity(
//                EditSettingsActivity.class).create().start().resume().get();
//    }
//
//    @Override
//    public int getSubmitButtonResId() {
//        return R.id.fab_EditProfile;
//    }
//
//    @Override
//    public void idActivity_fieldsAreRequired() {
//        setUserInputs("name", "");
//        mSubmitButton.performClick();
//
//        assertNull("Username error should update.", mUsername.getError());
//        assertNotNull("Password is required.", mPhoneNumber.getError());
//
//        setUserInputs("", "111111111");
//        mSubmitButton.performClick();
//
//        assertNotNull("Username is required.", mUsername.getError());
//        assertNull("Phone number error should update.", mPhoneNumber.getError());
//    }
//
//    @Test
//    public void onValidSubmit_returnsToSettings() {
//        submitValidUser();
//
////        assertTrue("Edit Profile should finish on successful submit.",
////                shadowOf(mActivity).isFinishing());
//    }
//
//    @Test
//    public void onCancel_nothingIsSaved() {
//        String oldUsername = Mocks.getMockReporter().name;
//        String oldPhone = Mocks.getMockReporter().phone;
//        assertCurrentReporterDetails(oldUsername, oldPhone);
//
//        setUserInputs("newUsername","newPhone");
//        submitValidUser();
//        mActivity.onBackPressed();
//
//        assertCurrentReporterDetails(oldUsername, oldPhone);
//        assertTrue("Edit Profile should finish on cancel.",
//                shadowOf(mActivity).isFinishing());
//    }
//}
