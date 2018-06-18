package com.github.codetanzania.feature.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.codetanzania.feature.company.NavItemActivity;
import com.github.codetanzania.feature.company.billing.BillingActivity;
import com.github.codetanzania.feature.list.IssueListActivity;
import com.github.codetanzania.open311.android.library.models.Category;
import com.github.codetanzania.feature.report.IssueCategoryPickerDialog;
import com.github.codetanzania.feature.report.ReportIssueActivity;
import com.github.codetanzania.feature.settings.SettingsActivity;
import com.github.codetanzania.util.LookAndFeelUtils;
import com.github.codetanzania.util.Util;

import tz.co.codetanzania.R;


public class MainActivity extends AppCompatActivity
        implements IssueCategoryPickerDialog.OnSelectIssueCategory, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_REPORT_ISSUE = 1;

    private View mLlReportIssue;
    private View mLlIssueList;
    private View mLlSeeMyBill;
    private View mLlNewConnection;

    /* the spinner dialog to let users select issue category */
    private IssueCategoryPickerDialog mIssueCategoryPickerDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = setupAndReturnActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LookAndFeelUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.iconPrimary));
        }
        setupNavigationDrawer(toolbar);

        mLlReportIssue = findViewById(R.id.ll_reportIssue);
        mLlIssueList = findViewById(R.id.ll_issueList);
        mLlSeeMyBill = findViewById(R.id.ll_seeMyBill);
        mLlNewConnection = findViewById(R.id.ll_newConnection);

        mLlReportIssue.setOnClickListener(this);
        mLlIssueList.setOnClickListener(this);
        mLlSeeMyBill.setOnClickListener(this);
        mLlNewConnection.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Toolbar setupAndReturnActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar_layout);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }

        return toolbar;
    }

    private void setupNavigationDrawer(Toolbar toolbar) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        final DrawerLayout drawerLayout   = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleNavigationItemSelection(item.getItemId(), drawerLayout);
                return true;
            }
        });

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.action_open_drawer, R.string.text_close_drawer) {
            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void handleNavigationItemSelection(int itemId, DrawerLayout drawerLayout) {
        int navItemId = -1;
        switch (itemId) {
            case R.id.report_issue:
                initializeIssueCategoryPickerDialog();
                drawerLayout.closeDrawers();
                break;
            case R.id.my_bill:
                seeMyBill();
                drawerLayout.closeDrawers();
                break;
            case R.id.payment_details:
                navItemId = NavItemActivity.PAY;
                break;
            case R.id.contact_us:
                navItemId = NavItemActivity.CONTACT;
                break;
            case R.id.tariffs:
                navItemId = NavItemActivity.TARIFFS;
                break;
            case R.id.extras:
                break;
            case R.id.faqs:
                navItemId = NavItemActivity.FAQ;
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                drawerLayout.closeDrawers();
                break;
            case R.id.home:
                drawerLayout.closeDrawers();
                break;
            case R.id.logout:
                Util.confirmExitApp(this);
                break;
            case R.id.request_new_connection:
                navItemId = NavItemActivity.REQUEST_NEW_CONNECTION;
                break;
            default:
                drawerLayout.closeDrawers();
                break;
        }

        if (navItemId != -1) {
            NavItemActivity.startNavActivity(this, navItemId);
            // drawerLayout.closeDrawers();
        }
    }

    private void seeReportedIssues() {
        Intent activityIntent = new Intent(this, IssueListActivity.class);
        startActivity(activityIntent);
    }

    private void seeMyBill() {
        Intent intent = new Intent(this, BillingActivity.class);
        startActivity(intent);
    }

    public void startReportIssueActivity(Category category) {
        Intent intent = new Intent(this, ReportIssueActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(ReportIssueActivity.TAG_SELECTED_SERVICE, category);
        intent.putExtras(extras);
        startActivityForResult(intent, REQUEST_CODE_REPORT_ISSUE);
    }

    @Override
    public void initializeIssueCategoryPickerDialog() {
        if (mIssueCategoryPickerDialog == null) {
            mIssueCategoryPickerDialog = new IssueCategoryPickerDialog(this);
        }
        mIssueCategoryPickerDialog.show();
    }

    @Override
    public void onIssueCategorySelected(Category category) {
        startReportIssueActivity(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_reportIssue :
                initializeIssueCategoryPickerDialog();
                break;
            case R.id.ll_issueList :
                seeReportedIssues();
                break;
            case R.id.ll_seeMyBill :
                seeMyBill();
                break;
            case R.id.ll_newConnection :
                NavItemActivity.startNavActivity(this, NavItemActivity.REQUEST_NEW_CONNECTION);
                break;
        }
    }
}
