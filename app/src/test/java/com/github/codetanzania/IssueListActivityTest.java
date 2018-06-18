//package com.github.codetanzania;
//
//import android.support.design.widget.FloatingActionButton;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.TextView;
//
//import com.github.codetanzania.shadows.ShadowUtils;
//import com.github.codetanzania.ui.activity.IssueListActivity;
//import com.github.codetanzania.feature.settings.SettingsActivity;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.Robolectric;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.annotation.Config;
//import org.robolectric.fakes.RoboMenuItem;
//import org.robolectric.shadows.ShadowActivity;
//import org.robolectric.shadows.support.v4.Shadows;
//
//import tz.co.codetanzania.BuildConfig;
//import tz.co.codetanzania.R;
//
//import static junit.framework.Assert.assertNotNull;
//import static junit.framework.Assert.assertTrue;
//
///**
// * This is used for testing the IssueListActivity.
// */
//
//@RunWith(RobolectricTestRunner.class)
//@Config(constants = BuildConfig.class, shadows = {ShadowUtils.class})
//public class IssueListActivityTest {
//    private IssueListActivity mActivity;
//    private FloatingActionButton fab;
//    private MenuItem menuRefresh;
//
//    @Before
//    public void setup() {
//        mActivity = Robolectric.buildActivity(
//                IssueListActivity.class).create().start().resume().get();
//
//        fab = (FloatingActionButton) mActivity.findViewById(R.id.fab_ReportIssue);
//
//        menuRefresh = new RoboMenuItem(R.id.item_refresh);
//        mActivity.onOptionsItemSelected(menuRefresh);
//        //menuRefresh = mActivity.findViewById(R.id.item_refresh);
//    }
//
//    @Test
//    public void issueListActivity_hasStarted() {
//        assertNotNull(fab);
//        assertNotNull(menuRefresh);
//    }
//
//    @Test
//    public void clickFab_shouldStartIssue() {
//
//    }
//
//
//
//    @Test
//    public void clickRefresh_shouldStartNetworkCall() {
//        mActivity.onOptionsItemSelected(menuRefresh);
////        ShadowActivity shadowActivity = Shadows.shadowOf(mActivity);
////        assertTrue(shadowActivity.isFinishing());
//    }
//}
