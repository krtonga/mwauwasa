package com.github.codetanzania.feature.company.payment;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.github.codetanzania.core.model.PaymentMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import tz.co.codetanzania.R;

public class PaymentInstructionsListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<PaymentMethod> mPaymentMethods;

    PaymentInstructionsListAdapter(Context context, List<PaymentMethod> paymentMethods) {
        this.mContext = context;
        this.mPaymentMethods = paymentMethods;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mPaymentMethods.get(groupPosition)
                .getSteps()[childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_payment_instructions_content, parent, false);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        boolean hasOneItem = getChildrenCount(groupPosition) == 1;

        txtListChild.setText(hasOneItem ? childText : String.format(Locale.getDefault(), "%d. %s", childPosition + 1, childText));
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mPaymentMethods.get(groupPosition).getSteps().length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mPaymentMethods.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mPaymentMethods.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        PaymentMethod paymentMethod = (PaymentMethod) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_payment_instructions_header_item, parent, false);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(paymentMethod.getHeadline());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
