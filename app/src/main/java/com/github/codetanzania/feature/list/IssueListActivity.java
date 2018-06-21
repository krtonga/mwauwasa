package com.github.codetanzania.feature.list;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.codetanzania.open311.android.library.EventHandler;
import com.github.codetanzania.open311.android.library.api.ReportService;
import com.github.codetanzania.open311.android.library.models.Category;
import com.github.codetanzania.open311.android.library.models.Problem;
import com.github.codetanzania.feature.report.IssueCategoryPickerDialog;
import com.github.codetanzania.feature.report.ReportIssueActivity;
import com.github.codetanzania.open311.android.library.models.Reporter;
import com.github.codetanzania.util.LookAndFeelUtils;
import com.github.codetanzania.util.Util;

import java.util.ArrayList;

import tz.co.codetanzania.R;


/* tickets activity. load and display tickets from the server */
public class IssueListActivity extends AppCompatActivity
    implements ErrorFragment.OnReloadClickListener,
        IssueCategoryPickerDialog.OnSelectIssueCategory,
        OnItemClickListener<Problem> {

    /* used by the logcat */
    private static final String TAG = "TicketGroupsActivity";

    /* qualifying token */
    public static final String KEY_ITEMS_QUALIFIER = "items";

    /* Fab to make a new issue */
    private FloatingActionButton mFab;

    /* An error flag */
    private boolean isErrorState = false;

    /* A menu flag */
    private boolean showMenu = true;
    private boolean isRefreshing = false;

    private String mPhoneNumber;
    private ArrayList<Problem> mRequests;

    /* IssuePicker Dialog */
    private IssueCategoryPickerDialog pickerDialog;

    private BroadcastReceiver mMyReportedProblemsReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isRefreshing = intent.getBooleanExtra(EventHandler.IS_PRELIMINARY_DATA, false);

            // If this call was a success...
            if (intent.getBooleanExtra(EventHandler.IS_SUCCESS, false)) {
                ArrayList<Problem> problems = intent.getParcelableArrayListExtra(EventHandler.REQUEST_LIST);

                // If the list of problems is empty
                if ((problems == null || problems.isEmpty())
                        && (mRequests == null || mRequests.isEmpty())) {
                    if (isRefreshing) {
                        // This result was from the db. Wait...
                        showLoadingFragment();
                    } else {
                        // This result was from the server. There are no reported problems...
                        showEmptyFragment();
                    }
                }
                // This list of problems contains items...
                else {
                    showListTabs(problems);
                }
            }

            // There was an error
            else if (mRequests == null || mRequests.isEmpty()) {
                // if no issues are shown display error fragment
                showErrorFragment();
            } else {
                // if issues are already shown from db/earlier, just show toast
                Toast.makeText(IssueListActivity.this, R.string.msg_server_error, Toast.LENGTH_LONG).show();            }
        }
    };

    /*
     * TODO: Add search and profile.
     * Menu items will be hidden when different fragment
     * than ServiceRequestsListFragment is committed
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If user has not signed in, then end activity
        Reporter reporter = Util.getCurrentReporter(getApplicationContext());
        if (reporter == null || TextUtils.isEmpty(reporter.getPhone())) {
            finish();
            return;
        }
        mPhoneNumber = reporter.getPhone();

        setContentView(R.layout.activity_issue_tickets_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.basic_toolbar_layout);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0f);
        }

        // Click on fab will start new report
        mFab = (FloatingActionButton) findViewById(R.id.fab_ReportIssue);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeIssueCategoryPickerDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LookAndFeelUtils.setupActionBar(this, R.string.title_issue_list, true);
        getReportedIssues();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMyReportedProblemsReceived);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.issues, menu);

        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(showMenu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem refresh = menu.findItem(R.id.item_refresh);
        MenuItem loading = menu.findItem(R.id.item_loading);

        if (showMenu) {
            // show loader bar or static refresh button
            if (isRefreshing) {
                loading.setVisible(true);
                refresh.setVisible(false);
            } else {
                loading.setVisible(false);
                refresh.setVisible(true);
            }
        } else {
            // show nothing
            loading.setVisible(false);
            refresh.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.item_refresh:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getReportedIssues() {
        if (mRequests == null) {
            LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(mMyReportedProblemsReceived,
                    new IntentFilter(EventHandler.BROADCAST_MY_PROBLEMS_FETCHED));
        }

        isRefreshing = true;
        ReportService.fetchProblems(getApplicationContext(), mPhoneNumber);
        showLoadingFragment();
    }

    /* show or hide menu items */
    private void showMenuItems(boolean show) {
        showMenu = show;
        // trigger call to `onCreateOptionsMenu`
        invalidateOptionsMenu();
    }

    private void refresh() {
        isRefreshing = true;
        getReportedIssues();
        invalidateOptionsMenu();
    }

    private void showLoadingFragment() {
        // hide controls. no need to show them while data is being loaded
        showMenuItems(false);

        ProgressBarFragment mProgressBarFrag = ProgressBarFragment.getInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frl_TicketsActivity, mProgressBarFrag)
                .disallowAddToBackStack()
                .commitAllowingStateLoss();
    }

    private void showEmptyFragment() {
        // hide controls. no need to show them while data is being loaded
        showMenuItems(false);

        EmptyIssuesFragment frag = EmptyIssuesFragment.getNewInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frl_TicketsActivity, frag)
                .disallowAddToBackStack()
                .commitAllowingStateLoss();
    }

    private void showErrorFragment() {
        if (isErrorState) {
            return;
        }

        isErrorState = true;

        // hide controls. no need to show them for server error
        showMenuItems(false);

        ErrorFragment mErrorFrag = ErrorFragment.getInstance(
                getString(R.string.msg_server_error), R.drawable.ic_cloud_off_48x48);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frl_TicketsActivity, mErrorFrag)
                .disallowAddToBackStack()
                .commitAllowingStateLoss();
    }

    private void showListTabs(ArrayList<Problem> requests) {
        // show service requests grouped into tabs
        ServiceRequestsTabFragment fragment = ServiceRequestsTabFragment.getNewInstance(requests);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frl_TicketsActivity, fragment)
                .disallowAddToBackStack()
                .commitAllowingStateLoss();

        // show menu items after items are displayed
        showMenuItems(true);
    }

    @Override
    public void onReloadClicked() {
        startActivity(getIntent());
        finish();
    }

    @Override
    public void onItemClick(Problem theItem) {
        // preview the item which was clicked
        Util.startPreviewIssueActivity(this, theItem);
    }

    @Override
    public void initializeIssueCategoryPickerDialog() {
        if (pickerDialog == null) {
            pickerDialog =
                    new IssueCategoryPickerDialog(this);
        }
        pickerDialog.show();
    }

    @Override
    public void onIssueCategorySelected(Category category) {
        Intent intent = new Intent(this, ReportIssueActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(ReportIssueActivity.TAG_SELECTED_SERVICE, category);
        intent.putExtras(extras);
        startActivityForResult(intent, 1);
    }
}
