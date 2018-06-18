//package com.github.codetanzania.utils;
//
//import android.content.Intent;
//import android.support.design.widget.TextInputLayout;
//import android.support.v4.app.FragmentActivity;
//import android.view.View;
//import android.widget.Button;
//
//import com.github.codetanzania.core.model.Reporter;
//import com.github.codetanzania.util.Util;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.RuntimeEnvironment;
//import org.robolectric.annotation.Config;
//import org.robolectric.shadows.ShadowIntent;
//import org.robolectric.shadows.ShadowLooper;
//
//import tz.co.codetanzania.BuildConfig;
//import tz.co.codetanzania.R;
//
//import static junit.framework.Assert.assertEquals;
//import static junit.framework.Assert.assertNull;
//import static org.junit.Assert.assertNotNull;
//import static org.robolectric.Shadows.shadowOf;
//
///**
// * This unit test is used to test the initial registration screen
// * shown when a user first opens the app.
// */
//@RunWith(RobolectricTestRunner.class)
//@Config(constants = BuildConfig.class, shadows = {})
//public abstract class ProfileFragmentTest {
//    protected FragmentActivity mActivity;
//
//    protected TextInputLayout mUsername;
//    //protected TextInputLayout mEmail;
//    protected TextInputLayout mAreaCode;
//    protected TextInputLayout mPhoneNumber;
//    protected View mSubmitButton;
//
//    public abstract FragmentActivity setActivity();
//    public abstract int getSubmitButtonResId();
//
//    @Before
//    public void setup() {
//        mActivity = setActivity();
//        mUsername = (TextInputLayout) mActivity.findViewById(R.id.til_UserName);
//        //mEmail = (TextInputLayout) mActivity.findViewById(R.id.til_Email);
//        mAreaCode = (TextInputLayout) mActivity.findViewById(R.id.til_AreaCode);
//        mPhoneNumber = (TextInputLayout) mActivity.findViewById(R.id.til_PhoneNumber);
//        mSubmitButton = mActivity.findViewById(getSubmitButtonResId());
//    }
//
//    @Test
//    public void idActivity_starts() {
//        assertNotNull(mUsername);
//        //assertNotNull(mEmail);
//        assertNotNull(mAreaCode);
//        assertNotNull(mPhoneNumber);
//        assertNotNull(mSubmitButton);
//    }
//
//    @Test
//    public void idActivity_fieldsAreRequired() {
//        mSubmitButton.performClick();
//
//        assertNotNull("Username is required.", mUsername.getError());
//        assertNotNull("Password is required.", mPhoneNumber.getError());
//
//        mUsername.getEditText().setText("name");
//        mSubmitButton.performClick();
//
//        assertNull("Username error should update.", mUsername.getError());
//        assertNotNull("Password is required.", mPhoneNumber.getError());
//
//        mUsername.getEditText().setText("");
//        mPhoneNumber.getEditText().setText("111111");
//        mSubmitButton.performClick();
//
//        assertNotNull("Username is required.", mUsername.getError());
//        assertNull("Phone number error should update.", mPhoneNumber.getError());
//    }
//
//    @Test
//    public void idActivity_areaCodeIsSetAsDefault() {
//        String defaultAreaCode = mActivity.getString(R.string.default_area_code);
//        String phoneNumber = "111111";
//
//        setUserInputs("name",phoneNumber);
//        mSubmitButton.performClick();
//        Reporter reporter = Util.getCurrentReporter(RuntimeEnvironment.application);
//        assertEquals("Un-edited area code should use default.",
//                defaultAreaCode+phoneNumber, reporter.phone);
//
//        mAreaCode.getEditText().setText(" ");
//        mSubmitButton.performClick();
//        reporter = Util.getCurrentReporter(RuntimeEnvironment.application);
//        assertEquals("Blank area code should use default.",
//                defaultAreaCode+phoneNumber, reporter.phone);
//    }
//
//    @Test
//    public void onValidSubmit_reporterIsSaved() {
//        String name = "name";
//        String areacode = "1";
//        String number = "111111";
//
//        mAreaCode.getEditText().setText(areacode);
//        setUserInputs(name, number);
//        mSubmitButton.performClick();
//
//        assertCurrentReporterDetails(name, areacode+number);
//    }
//
//    public void setUserInputs(String name, String phone) {
//        if (name != null) {
//            mUsername.getEditText().setText(name);
//        }
//        if (phone != null) {
//            mPhoneNumber.getEditText().setText(phone);
//        }
//    }
//
//    public void submitValidUser() {
//        setUserInputs("name","111111");
//        mSubmitButton.performClick();
//    }
//
//    public Class getClassOfStartedActivity() {
//        Intent startedIntent = shadowOf(mActivity).getNextStartedActivity();
//        return shadowOf(startedIntent).getIntentClass();
//    }
//
//    public static void assertCurrentReporterDetails(String name, String number) {
//        Reporter reporter = Util.getCurrentReporter(RuntimeEnvironment.application);
//        assertEquals("Name should be saved.", name, reporter.name);
//        assertEquals("Area code and phone number should be saved.",
//                number, reporter.phone);
//    }
//}
