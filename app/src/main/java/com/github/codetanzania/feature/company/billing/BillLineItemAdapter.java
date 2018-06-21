package com.github.codetanzania.feature.company.billing;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.codetanzania.open311.android.library.models.customer.Bill;
import com.github.codetanzania.open311.android.library.models.customer.LineItem;
import com.github.codetanzania.open311.android.library.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import tz.co.codetanzania.R;

public class BillLineItemAdapter extends SimpleLineItemAdapter {
    private static final int NESTED_VIEW_TYPE = 1;

    public static BillLineItemAdapter getInstance(Bill bill, String name, String accountNumber, Resources resources) {
        List<LineItemDisplay> displayList = new ArrayList<>(bill.getItems().size() + 4);

        displayList.add(new LineItemDisplay("Control Number", bill.getNumber(), R.color.colorAccent));

        List<LineItemDisplay> accountHolderList = new ArrayList<>(1);
        String label = resources.getString(R.string.bill_name);
        accountHolderList.add(new LineItemDisplay(label, name));
        label = resources.getString(R.string.bill_number);
        LineItemDisplay accountHolder = new LineItemDisplay(label, accountNumber);
        accountHolder.setNested(accountHolderList);
        displayList.add(accountHolder);

        List<LineItemDisplay> periodList = new ArrayList<>(3);
        label = resources.getString(R.string.bill_issued);
        periodList.add(new LineItemDisplay(label, DateUtils.formatForDisplay(bill.getPeriod().getBilledAt())));
        label = resources.getString(R.string.bill_period_start);
        periodList.add(new LineItemDisplay(label, DateUtils.formatForDisplay(bill.getPeriod().getStartedAt())));
        label = resources.getString(R.string.bill_period_end);
        periodList.add(new LineItemDisplay(label, DateUtils.formatForDisplay(bill.getPeriod().getEndedAt())));
        label = resources.getString(R.string.bill_due);
        LineItemDisplay period = new LineItemDisplay(label, DateUtils.formatForDisplay(bill.getPeriod().getDuedAt()));
        period.setNested(periodList);
        displayList.add(period);

        displayList.addAll(convert(bill.getItems(), bill.getCurrency()));

        List<LineItemDisplay> balanceList = new ArrayList<>(4);
        label = resources.getString(R.string.bill_opening);
        balanceList.add(new LineItemDisplay(label, String.valueOf(bill.getBalance().getOpen())));
        label = resources.getString(R.string.bill_charges);
        balanceList.add(new LineItemDisplay(label, String.valueOf(bill.getBalance().getCharges())));
        label = resources.getString(R.string.bill_closing);
        balanceList.add(new LineItemDisplay(label, String.valueOf(bill.getBalance().getClose())));
        label = resources.getString(R.string.bill_debt);
        balanceList.add(new LineItemDisplay(label, String.valueOf(bill.getBalance().getDebt())));
        label = resources.getString(R.string.bill_outstanding);
        LineItemDisplay balance = new LineItemDisplay(label, String.valueOf(bill.getBalance().getOutstanding()));
        balance.setNested(balanceList);
        displayList.add(balance);

        return new BillLineItemAdapter(displayList);
    }

    private static List<LineItemDisplay> convert(List<LineItem> items, String currency) {
        if (items == null) {
            return null;
        }
        List<LineItemDisplay> toDisplay = new ArrayList<>(items.size());
        for (LineItem item : items) {
            toDisplay.add(convert(item, currency));
        }
        return toDisplay;
    }

    private static LineItemDisplay convert(LineItem item, String currency) {
        if (item == null) {
            return null;
        }

        String label = item.getTime() == null ?
                null : DateUtils.formatForDisplay(item.getTime());

        String start = item.getName();

        String center = item.getQuantity() == null ? null :
                TextUtils.isEmpty(item.getUnit()) ?
                        String.valueOf(item.getQuantity())
                        : (item.getQuantity() +" "+ item.getUnit());

        String end = item.getPrice() == null ? null : TextUtils.isEmpty(currency) ?
                String.valueOf(item.getPrice()) : (currency +" "+ item.getPrice());

        return new LineItemDisplay(label, start, center, end,
                    convert(item.getItems(), currency), R.color.colorAccent);
    }

    public BillLineItemAdapter(List<LineItemDisplay> lineItems) {
        super(lineItems);
    }

    @Override
    public LineItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_billing, parent, false);

        switch (viewType) {
            case SIMPLE_VIEW_TYPE: return new LineItemViewHolder(view);
            case NESTED_VIEW_TYPE: return new NestedLineItemViewHolder(view);
            default: return null;
        }
    }

    @Override
    public void onBindViewHolder(LineItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (holder.getItemViewType() == NESTED_VIEW_TYPE) {
            ((NestedLineItemViewHolder) holder)
                    .setSubItems(mLineItems.get(position).getNested());
        }
    }

    @Override
    public int getItemViewType(int position) {
        List<LineItemDisplay> subItems = mLineItems.get(position).getNested();
        if (subItems == null || subItems.size() == 0) {
            return SIMPLE_VIEW_TYPE;
        } else {
            return NESTED_VIEW_TYPE;
        }
    }

    static class NestedLineItemViewHolder extends LineItemViewHolder {
        int type = NESTED_VIEW_TYPE;

        RecyclerView rvSubItems;
        SimpleLineItemAdapter mAdapter;

        NestedLineItemViewHolder(View itemView) {
            super(itemView);

            rvSubItems = (RecyclerView) itemView.findViewById(R.id.rv_sub_items);
            rvSubItems.setLayoutManager(new LinearLayoutManager(
                            itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        }

        void setSubItems(List<LineItemDisplay> subItems) {
            mAdapter = new SimpleLineItemAdapter(subItems);
            rvSubItems.setAdapter(mAdapter);
        }
    }
}
