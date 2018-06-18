package com.github.codetanzania.feature.company.billing;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.github.codetanzania.open311.android.library.api.models.ApiAccountAccessor;
import com.github.codetanzania.open311.android.library.api.models.ApiBill;
import com.github.codetanzania.open311.android.library.api.models.ApiCustomerAccount;
import com.github.codetanzania.open311.android.library.models.Reporter;
import com.github.codetanzania.open311.android.library.models.customer.Accessor;
import com.github.codetanzania.open311.android.library.models.customer.Bill;
import com.github.codetanzania.open311.android.library.models.customer.CustomerAccount;
import com.github.codetanzania.open311.android.library.models.customer.LineItem;
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

    private ScrollView mScrollView;

    private BarChart mChart;
    private List<String> mChartLabels;
    private List<BarEntry> mBars;

    private Spinner mSpinner;
    private List<String> mSpinnerLabels;

    private FrameLayout mFlBills;
    private LinearLayout mLlCardDue;
    private TextView mTvDueAmount;

    private ArrayList<RecyclerView> mBillViews;
    private int mCurrentView;

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
        mScrollView = (ScrollView) view.findViewById(R.id.sv_billing);
        mFlBills = (FrameLayout) view.findViewById(R.id.fl_bills);
        mLlCardDue = (LinearLayout) view.findViewById(R.id.ll_amountDue);
        mTvDueAmount = (TextView) mLlCardDue.findViewById(R.id.content);

        // style toolbar
        setHasOptionsMenu(true);
        mListener.changeToolbarTitle(R.string.bill_mini_statement_title);

        // initialize chart
        styleChart();

        // add data
        displayStatements();

        return view;
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
    private void displayStatements() {
        ArrayList<Bill> bills = mAccount.getBills();

        // initialize arrays
        mSpinnerLabels = new ArrayList<>(bills.size());
        mChartLabels = new ArrayList<>(bills.size());
        mBillViews = new ArrayList<>(bills.size());
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
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RecyclerView billView = (RecyclerView) inflater.inflate(R.layout.recyler_view, null);

        // fill in any details dynamically here
        RecyclerView rvlineItems = (RecyclerView) billView.findViewById(R.id.rv_empty);
        rvlineItems.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));
        rvlineItems.setAdapter(new NestedLineItemAdapter(bill.getItems(), bill.getCurrency()));

        // save for later use
        mBillViews.add(billView);
    }

    private void showStatement(int position) {
        // This is sometimes triggered by an async task, therefore check that fragment is inflated
        if (mFlBills == null || getActivity() == null) {
            return;
        }

        // remove previous statement
        if (mCurrentView != position) {
            mFlBills.removeAllViews();

            // attach view
            if (mBillViews.size() < position) {
                mFlBills.addView(mBillViews.get(position), 0, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }

            mCurrentView = position;
        }

        // highlight chart bar without calling listener
        mChart.highlightValue(new Highlight(mSpinnerLabels.size() - position, 0, 0));

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

    static class MocksAcnt {
        private static String mockNumber = "92389";
        private static String mockName = "Marlon Lang";
        private static String mockPhone = "255719818179";
        private static String mockEmail = "thad_konopelski@gmail.com";
        private static String mockNeighborhood = "Spinkamouth";
        private static String mockAddress = "391 Weimann Island";
        private static String mockLocale = "en";
        private static double mockLat = 34.49401440942532;
        private static double mockLong = -62.53416466705956;
        private static Boolean mockActiveAt = true;
        private static String mockId = "5b229dc1a876bf0004db6e57";
        private static String mockUpdatedAt = "2018-06-14T16:54:25.482Z";
        private static String mockCreatedAt = "2018-06-14T16:54:25.482Z";

        private static String mockAccessorLocale = "sw";
        private static String mockAccessorName = "Naomi Powlowski";
        private static String mockAccessorPhone = "255756995672";
        private static String mockAccessorEmail = "deon_murphy@hotmail.com";
        private static String mockAccessorCreatedAt = "2018-06-14T16:54:25.478Z";
        private static String mockAccessorUpdatedAt = "2018-06-14T16:54:25.488Z";
        private static String mockAccessorVerifiedAt = "2018-06-14T16:54:25.488Z";

        private static String mockSubItemName = "Previous Readings";
        private static Integer mockSubItemQuantity = 832;
        private static String mockSubItemUnit = "cbm";
        private static String mockSubItemTime = "2018-05-14T16:54:24.804Z";
        private static Float mockSubItemPrice = 1453f;

        private static String mockItemName = "Unit Consumed";
        private static Integer mockItemQuantity = 427;
        private static String mockItemUnit = "m3";
        private static String mockItemTime = "2018-05-15T16:54:24.804Z";
        private static Float mockItemPrice = 73f;
        // static String mockItemTotal = ""; Should exist?

        private static String mockBillNumber = "14517";
        private static String mockBilledAt = "2018-05-14T16:54:24.804Z";
        private static String mockBillStartedAt = "2018-04-14T16:54:24.804Z";
        private static String mockBillEndedAt = "2018-05-14T16:54:24.804Z";
        private static String mockBillDuedAt = "2018-06-14T16:54:24.804Z";
        private static Float mockBillOutstanding = 2586f;
        private static Float mockBillOpen = 46f;
        private static Float mockBillCharges = 150f;
        private static Float mockBillDebt = 311f;
        private static Float mockBillClose = 437f;
        private static String mockBillCurrency = "UYU UYI";
        private static String mockBillNotes = "Praesentium dolores debitis occaecati.";

        static String sampleJsonResponse = "{\n" +
                "    \"number\": \""+mockNumber+"\",\n" +
                "    \"name\": \""+mockName+"\",\n" +
                "    \"phone\": \""+mockPhone+"\",\n" +
                "    \"email\": \""+mockEmail+"\",\n" +
                "    \"neighborhood\": \""+mockNeighborhood+"\",\n" +
                "    \"address\": \""+mockAddress+"\",\n" +
                "    \"locale\": \""+mockLocale+"\",\n" +
                "    \"location\": {\n" +
                "        \"type\": \"Point\",\n" +
                "        \"coordinates\": [\n" +
                "            "+mockLong+",\n" +
                "            "+mockLat+"\n" +
                "        ]\n" +
                "    },\n" +
                "    \"accessors\": [\n" +
                "        {\n" +
                "            \"locale\": \""+mockAccessorLocale+"\",\n" +
                "            \"name\": \""+mockAccessorName+"\",\n" +
                "            \"phone\": \""+mockAccessorPhone+"\",\n" +
                "            \"email\": \""+mockAccessorEmail+"\",\n" +
                "            \"createdAt\": \""+mockAccessorCreatedAt+"\",\n" +
                "            \"updatedAt\": \""+mockAccessorUpdatedAt+"\",\n" +
                "            \"verifiedAt\": \""+mockAccessorVerifiedAt+"\"\n" +
                "        }" +
                "    ],\n" +
                "    \"bills\": [\n" +
                "        {\n" +
                "            \"items\": [\n" +
                "                {\n" +
                "                    \"items\": [\n" +
                "                        {\n" +
                "                            \"name\": \""+mockSubItemName+"\",\n" +
                "                            \"quantity\": "+ mockSubItemQuantity +",\n" +
                "                            \"unit\": \""+mockSubItemUnit+"\",\n" +
                "                            \"time\": \""+mockSubItemTime+"\",\n" +
                "                            \"price\": \""+mockSubItemPrice+"\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"name\": \""+mockItemName+"\",\n" +
                "                    \"quantity\": "+mockItemQuantity+",\n" +
                "                    \"unit\": \""+mockItemUnit+"\",\n" +
                "                    \"price\": \""+mockItemPrice+"\",\n" +
                "                    \"time\": \""+mockItemTime+"\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"number\": \""+mockBillNumber+"\",\n" +
                "            \"period\": {\n" +
                "                \"billedAt\": \""+mockBilledAt+"\",\n" +
                "                \"startedAt\": \""+ mockBillStartedAt +"\",\n" +
                "                \"endedAt\": \""+ mockBillEndedAt +"\",\n" +
                "                \"duedAt\": \""+ mockBillDuedAt +"\"\n" +
                "            },\n" +
                "            \"balance\": {\n" +
                "                \"outstand\": "+ mockBillOutstanding +",\n" +
                "                \"open\": "+ mockBillOpen +",\n" +
                "                \"charges\": "+ mockBillCharges +",\n" +
                "                \"debt\": "+ mockBillDebt +",\n" +
                "                \"close\": "+ mockBillClose +"\n" +
                "            },\n" +
                "            \"currency\": \""+ mockBillCurrency +"\",\n" +
                "            \"notes\": \""+ mockBillNotes +"\"\n" +
                "        }"+
                "    ],\n" +
                "    \"active\": "+mockActiveAt+",\n" +
                "    \"_id\": \""+mockId+"\",\n" +
                "    \"updatedAt\": \""+mockUpdatedAt+"\",\n" +
                "    \"createdAt\": \""+mockCreatedAt+"\"\n" +
                "}";

        public static void assertAccountMatchesMock(ApiCustomerAccount account) {
            assertEquals("Number should be correct", mockNumber, account.number);
            assertEquals("Name should be correct", mockName, account.name);
            assertEquals("Phone should be correct", mockPhone, account.phone);
            assertEquals("Email should be correct", mockEmail, account.email);
            assertEquals("Neighborhood should be correct", mockNeighborhood, account.neighborhood);
            assertEquals("Address should be correct", mockAddress, account.address);
            assertEquals("Locale should be correct", mockLocale, account.locale);
            assertEquals("Lat should be correct", mockLat, account.location.getLatitude());
            assertEquals("Lng should be correct", mockLong, account.location.getLongitude());
            assertEquals("ActiveAt should be correct", mockActiveAt, account.active);
            assertEquals("Id should be correct", mockId, account._id);
            assertEquals("UpdatedAt should be correct", mockUpdatedAt, account.updatedAt);
            assertEquals("CreatedAt should be correct", mockCreatedAt, account.createdAt);

            ApiAccountAccessor accessor = account.accessors[0];
            assertEquals("AccessorLocale should be correct", mockAccessorLocale, accessor.getLocale());
            assertEquals("AccessorName should be correct", mockAccessorName, accessor.getName());
            assertEquals("AccessorPhone should be correct", mockAccessorPhone, accessor.getPhone());
            assertEquals("AccessorEmail should be correct", mockAccessorEmail, accessor.getEmail());
            assertEquals("AccessorCreatedAt should be correct", mockAccessorCreatedAt, accessor.getCreatedAt());
            assertEquals("AccessorUpdatedAt should be correct", mockAccessorUpdatedAt, accessor.getUpdatedAt());
            assertEquals("AccessorVerifiedAt should be correct", mockAccessorVerifiedAt, accessor.getVerifiedAt());

            ApiBill bill = account.bills[0];
            ApiBill.Item item = bill.items[0];
            ApiBill.Item subItem = item.items[0];
            assertEquals("SubItemName should be correct", mockSubItemName, subItem.name);
            assertEquals("SubItemQuantity should be correct", mockSubItemQuantity, subItem.quantity);
            assertEquals("SubItemUnit should be correct", mockSubItemUnit, subItem.unit);
            assertEquals("SubItemTime should be correct", mockSubItemTime, subItem.time);
            assertEquals("SubItemPrice should be correct", mockSubItemPrice, subItem.price);

            assertEquals("ItemName should be correct",  mockItemName, item.name);
            assertEquals("ItemQuantity should be correct", mockItemQuantity, item.quantity);
            assertEquals("ItemUnit should be correct", mockItemUnit, item.unit);
            assertEquals("ItemTime should be correct", mockItemTime, item.time);
            assertEquals("ItemPrice should be correct", mockItemPrice, item.price);
            // static String mockItemTotal = ""; Should exist?

            assertEquals("BillNumber should be correct", mockBillNumber, bill.number);
            assertEquals("BilledAt should be correct", mockBilledAt, bill.period.billedAt);
            assertEquals("BillStartedAt should be correct", mockBillStartedAt, bill.period.startedAt);
            assertEquals("BillEndedAt should be correct", mockBillEndedAt, bill.period.endedAt);
            assertEquals("BillDuedAt should be correct", mockBillDuedAt, bill.period.duedAt);
            assertEquals("BillOutstanding should be correct", mockBillOutstanding, bill.balance.outstand);
            assertEquals("BillOpen should be correct", mockBillOpen, bill.balance.open);
            assertEquals("BillCharges should be correct", mockBillCharges, bill.balance.charges);
            assertEquals("BillDebt should be correct", mockBillDebt, bill.balance.debt);
            assertEquals("BillClose should be correct", mockBillClose, bill.balance.close);
            assertEquals("BillCurrency should be correct", mockBillCurrency, bill.currency);
            assertEquals("BillNotes should be correct", mockBillNotes, bill.notes);
        }

        public static void assertAccountMatchesMock(CustomerAccount account) {
            assertEquals("Number should be correct", mockNumber, account.getNumber());
            assertEquals("Name should be correct", mockName, account.getName());
            assertEquals("Phone should be correct", mockPhone, account.getPhone());
            assertEquals("Email should be correct", mockEmail, account.getEmail());
            assertEquals("Neighborhood should be correct", mockNeighborhood, account.getNeighborhood());
            assertEquals("Address should be correct", mockAddress, account.getAddress());
            assertEquals("Locale should be correct", mockLocale, account.getLocale());
            assertEquals("Lat should be correct", mockLat, account.getLocation().getLatitude());
            assertEquals("Lng should be correct", mockLong, account.getLocation().getLongitude());
            assertEquals("ActiveAt should be correct", mockActiveAt, account.isActive());
            assertEquals("Id should be correct", mockId, account.getId());
            assertEquals("UpdatedAt should be correct", mockUpdatedAt, DateUtils.formatForServer(account.getUpdatedAt()));
            assertEquals("CreatedAt should be correct", mockCreatedAt, DateUtils.formatForServer(account.getCreatedAt()));

            Accessor accessor = account.getAccessors().get(0);
            assertEquals("AccessorLocale should be correct", mockAccessorLocale, accessor.getLocale());
            assertEquals("AccessorName should be correct", mockAccessorName, accessor.getName());
            assertEquals("AccessorPhone should be correct", mockAccessorPhone, accessor.getPhone());
            assertEquals("AccessorEmail should be correct", mockAccessorEmail, accessor.getEmail());
            assertEquals("AccessorCreatedAt should be correct", mockAccessorCreatedAt, DateUtils.formatForServer(accessor.getCreatedAt()));
            assertEquals("AccessorUpdatedAt should be correct", mockAccessorUpdatedAt, DateUtils.formatForServer(accessor.getUpdatedAt()));
            assertEquals("AccessorVerifiedAt should be correct", mockAccessorVerifiedAt, DateUtils.formatForServer(accessor.getVerifiedAt()));

            Bill bill = account.getBills().get(0);
            LineItem item = bill.getItems().get(0);
            LineItem subItem = item.getItems().get(0);
            assertEquals("SubItemName should be correct", mockSubItemName, subItem.getName());
            assertEquals("SubItemQuantity should be correct", mockSubItemQuantity, subItem.getQuantity());
            assertEquals("SubItemUnit should be correct", mockSubItemUnit, subItem.getUnit());
            assertEquals("SubItemTime should be correct", mockSubItemTime, DateUtils.formatForServer(subItem.getTime()));
            assertEquals("SubItemPrice should be correct", mockSubItemPrice, subItem.getPrice());

            assertEquals("ItemName should be correct",  mockItemName, item.getName());
            assertEquals("ItemQuantity should be correct", mockItemQuantity, item.getQuantity());
            assertEquals("ItemUnit should be correct", mockItemUnit, item.getUnit());
            assertEquals("ItemTime should be correct", mockItemTime, DateUtils.formatForServer(item.getTime()));
            assertEquals("ItemPrice should be correct", mockItemPrice, item.getPrice());
            // static String mockItemTotal = ""; Should exist?

            assertEquals("BillNumber should be correct", mockBillNumber, bill.getNumber());
            assertEquals("BilledAt should be correct", mockBilledAt, DateUtils.formatForServer(bill.getPeriod().getBilledAt()));
            assertEquals("BillStartedAt should be correct", mockBillStartedAt, DateUtils.formatForServer(bill.getPeriod().getStartedAt()));
            assertEquals("BillEndedAt should be correct", mockBillEndedAt, DateUtils.formatForServer(bill.getPeriod().getEndedAt()));
            assertEquals("BillDuedAt should be correct", mockBillDuedAt, DateUtils.formatForServer(bill.getPeriod().getDuedAt()));
            assertEquals("BillOutstanding should be correct", mockBillOutstanding, bill.getBalance().getOutstanding());
            assertEquals("BillOpen should be correct", mockBillOpen, bill.getBalance().getOpen());
            assertEquals("BillCharges should be correct", mockBillCharges, bill.getBalance().getCharges());
            assertEquals("BillDebt should be correct", mockBillDebt, bill.getBalance().getDebt());
            assertEquals("BillClose should be correct", mockBillClose, bill.getBalance().getClose());
            assertEquals("BillCurrency should be correct", mockBillCurrency, bill.getCurrency());
            assertEquals("BillNotes should be correct", mockBillNotes, bill.getNotes());
        }
    }
}
