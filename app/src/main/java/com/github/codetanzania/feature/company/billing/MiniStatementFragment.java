package com.github.codetanzania.feature.company.billing;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.codetanzania.core.Analytics;
import com.github.codetanzania.open311.android.library.models.Reporter;
import com.github.codetanzania.open311.android.library.models.customer.Bill;
import com.github.codetanzania.open311.android.library.models.customer.CustomerAccount;
import com.github.codetanzania.open311.android.library.utils.DateUtils;
import com.github.codetanzania.util.Util;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tz.co.codetanzania.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static junit.framework.Assert.assertEquals;

/**
 * This gets the last four months of client water statements
 */

public class MiniStatementFragment extends Fragment
        implements AdapterView.OnItemSelectedListener, OnChartValueSelectedListener {

    private static String ACCOUNT_INTENT = "account-intent";

    private TitleChangeListener mListener;
    private String mAccountNumber;

    private CustomerAccount mAccount;

    private NestedScrollView mScrollView;

    private BarChart mChart;
    private List<String> mChartLabels;
    private List<BarEntry> mBars;

    private Spinner mSpinner;
    private List<String> mSpinnerLabels;

    private FrameLayout mFlBills;
    private LinearLayout mLlCardDue;
    private TextView mTvDueAmount;

    private ArrayList<View> mStatementViews;
    private int mCurrentView = -1;

    /**
     * Use this to create a new instance. This fragment must be passed an account
     * object, as it has no logic to retrieve an account itself.
     * @param account Used for display
     * @return a new fragment
     */
    public static MiniStatementFragment getInstance(CustomerAccount account) {
        Bundle args = new Bundle();
        args.putParcelable(ACCOUNT_INTENT, account);

        MiniStatementFragment fragment = new MiniStatementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Implementing class must implement TitleChangeListener
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (TitleChangeListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement TitleChange listener");
        }
    }

    /*
     * This ensures that bills shown are from correct account
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get account number
        Reporter reporter = Util.getCurrentReporter(getContext());
        if (reporter == null) {
            getActivity().finish();
            return;
        }
        mAccountNumber = reporter.getAccount();

        // extract account object from intent
        mAccount = getArguments().getParcelable(ACCOUNT_INTENT);

        // ensure that account has proper account number
        if (mAccount != null
                && !mAccountNumber.equals(mAccount.getNumber())) {
            // if account object is wrong, exit
            getActivity().finish();
            Analytics.onWrongAccountSent(mAccountNumber, mAccount.getNumber());
        }
    }

    /**
     * Initializes all views, styles fragment, and displays data from passed account object.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate view
        View view = inflater.inflate(R.layout.frag_billing, container, false);

        // assign all views
        mChart = (BarChart) view.findViewById(R.id.chart_consumption);
        mSpinner = (Spinner) view.findViewById(R.id.spnr_pick_bill);
        mScrollView = (NestedScrollView) view.findViewById(R.id.sv_billing);
        mFlBills = (FrameLayout) view.findViewById(R.id.fl_bills);
        mLlCardDue = (LinearLayout) view.findViewById(R.id.ll_amountDue);
        mTvDueAmount = (TextView) mLlCardDue.findViewById(R.id.content);

        // style toolbar
        setHasOptionsMenu(true);
        mListener.changeToolbarTitle(R.string.bill_mini_statement_title);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // initialize chart
        styleChart();

        // add data
        prepareStatementsAndDisplayChart();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.billing, menu);
    }

    /**
     * Menu options contain one item: Current Bill. On selection, display bill.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        scrollToBill(0);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Used to initialize chart style before data is set.
     */
    private void styleChart() {
        mChart.setDrawGridBackground(false);
        mChart.setDescription(null);
        mChart.setPinchZoom(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        mChart.getAxisRight().setEnabled(false);
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setValueFormatter(new LargeValueFormatter());
    }

    /*
     * Spinner allows user to change between the different bills sent in the account object.
     */
    private void prepareStatementsAndDisplayChart() {
        ArrayList<Bill> bills = mAccount.getBills();

        // initialize arrays
        mSpinnerLabels = new ArrayList<>(bills.size());
        mChartLabels = new ArrayList<>(bills.size());
        mStatementViews = new ArrayList<>(bills.size());
        mBars = new ArrayList<>(bills.size());

        for (int i = 0; i < bills.size(); i++) {
            Bill bill = bills.get(i);

            // add to labels
            addToLabels(bill);

            // add to chart
            float consumption = bill.getBalance().getCharges();
            mBars.add(new BarEntry(bills.size() - 1 - i, consumption));

            // prepare recycler view
            createStatement(bill);
        }

        // draw chart
        redrawChart();

        // allow user to switch between bills
        populateSpinner();

        // set due amount to this month
        mTvDueAmount.setText(getString(R.string.bill_due_value,
                String.valueOf(bills.get(0).getBalance().getOutstanding())));

        // show first statement
        showStatement(0);
    }

    // TODO add sort by date to bill
    private void addToLabels(Bill bill) {
        Calendar billedAt = bill.getPeriod().getBilledAt();
        String month = DateUtils.getMonthString(billedAt);

        // spinner labels are in order delivered
        mSpinnerLabels.add(month);

        // chart labels are in reverse order
        mChartLabels.add(0, month);
    }

    private void createStatement(Bill bill) {
        // setup statement
        RecyclerView billView = new RecyclerView(getContext());
        billView.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));
        billView.setAdapter(BillLineItemAdapter.getInstance(
                bill, mAccount.getName(), mAccountNumber, getResources()));

        // add view to fragment, with visibility gone
        billView.setVisibility(GONE);
        mFlBills.addView(billView, 0, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        // save for later access
        mStatementViews.add(billView);
    }

    private void showStatement(final int position) {
        // This is sometimes triggered by an async task, therefore check that fragment is inflated
        if (mFlBills == null || getActivity() == null) {
            return;
        }

        // remove previous statement
        if (mCurrentView != position) {
            if (mCurrentView != -1 && !mStatementViews.isEmpty()) {
                mStatementViews.get(mCurrentView).setVisibility(GONE);
            }

            // attach view
            if (mStatementViews.size() > position) {
                mStatementViews.get(position).setVisibility(VISIBLE);
            }

            mCurrentView = position;
        }

        // highlight chart bar without calling listener
        mChart.highlightValue(new Highlight(mSpinnerLabels.size() - position - 1, 0, 0));

        // update spinner
        mSpinner.setSelection(position);
    }

    private void populateSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, mSpinnerLabels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
    }

    private void redrawChart() {
        mChart.getXAxis().setValueFormatter(
                new IndexAxisValueFormatter(mChartLabels));

        BarDataSet bars = new BarDataSet(mBars, getString(R.string.bill_chart_label));
        bars.setColor(ContextCompat.getColor(getContext(), R.color.iconPrimary));

        // set bars to chart
        BarData dataSet = new BarData(bars);
        dataSet.setValueFormatter(new LargeValueFormatter());
        mChart.setData(dataSet);
        mChart.setOnChartValueSelectedListener(this);

        // trigger chart refresh
        mChart.invalidate();
    }

    private void scrollToBill(final int billIndex) {
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mSpinner.setSelection(billIndex);
                mScrollView.scrollTo(0, mScrollView.getBottom());
            }
        });
    }

    // spinner listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        showStatement(position);
    }

    // spinner listener
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        showStatement(0);
    }

    // chart bar selected listener
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        scrollToBill(mSpinnerLabels.size()-(int)e.getX());
    }

    // chart bar selected listener
    @Override
    public void onNothingSelected() {

    }

    public interface TitleChangeListener {
        void changeToolbarTitle(int newTitle);
    }
}
