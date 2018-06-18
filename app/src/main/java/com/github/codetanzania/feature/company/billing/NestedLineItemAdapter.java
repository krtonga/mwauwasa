package com.github.codetanzania.feature.company.billing;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.codetanzania.open311.android.library.models.customer.LineItem;

import java.util.List;

import tz.co.codetanzania.R;

public class NestedLineItemAdapter extends SimpleLineItemAdapter {
    private static final int NESTED_VIEW_TYPE = 1;

    public NestedLineItemAdapter(List<LineItem> lineItems, String currency) {
        super(lineItems, currency);
    }

    @Override
    public LineItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_billing, parent, false);

        switch (viewType) {
            case SIMPLE_VIEW_TYPE: return new LineItemViewHolder(view);
            case NESTED_VIEW_TYPE: return new LineItemViewHolder(view);
            default: return null;
        }
    }

    @Override
    public void onBindViewHolder(LineItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (holder.type == NESTED_VIEW_TYPE) {
            ((NestedLineItemViewHolder) holder)
                    .setSubItems(mLineItems.get(position).getItems(), mCurrency);
        }
    }

    @Override
    public int getItemViewType(int position) {
        List<LineItem> subItems = mLineItems.get(position).getItems();
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

        void setSubItems(List<LineItem> subItems, String currency) {
            mAdapter = new SimpleLineItemAdapter(subItems, currency);
            rvSubItems.setAdapter(mAdapter);
        }
    }
}
