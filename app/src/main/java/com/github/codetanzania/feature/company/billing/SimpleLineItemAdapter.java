package com.github.codetanzania.feature.company.billing;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.codetanzania.open311.android.library.models.customer.LineItem;
import com.github.codetanzania.open311.android.library.utils.DateUtils;

import java.util.List;

import tz.co.codetanzania.R;

public class SimpleLineItemAdapter extends RecyclerView.Adapter<SimpleLineItemAdapter.LineItemViewHolder> {
    static final int SIMPLE_VIEW_TYPE = 0;

    List<LineItem> mLineItems;
    String mCurrency;

    SimpleLineItemAdapter(List<LineItem> lineItems, String currency) {
        mLineItems = lineItems;
        mCurrency = currency;
    }

    @Override
    public LineItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_billing, parent, false);
        return new LineItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LineItemViewHolder holder, int position) {
        LineItem item = mLineItems.get(position);

        holder.tvLabel.setText(item.getName());

        if (item.getTime() != null) {
            holder.tvDate.setText(DateUtils.formatForDisplay(item.getTime()));
        }

        if (item.getQuantity() != null) {
            String quantity = TextUtils.isEmpty(item.getUnit()) ?
                    String.valueOf(item.getQuantity()) : (item.getQuantity() +" "+ item.getUnit());
            holder.tvDate.setText(quantity);
        }

        if (item.getPrice() != null) {
            String price = TextUtils.isEmpty(mCurrency) ?
                    String.valueOf(item.getPrice()) : (mCurrency +" "+ item.getPrice());
            holder.tvDate.setText(price);
        }
    }

    @Override
    public int getItemCount() {
        return mLineItems.size();
    }

    static class LineItemViewHolder extends RecyclerView.ViewHolder {
        int type = SIMPLE_VIEW_TYPE;

        TextView tvDate;
        TextView tvLabel;
        TextView tvQuantity;
        TextView tvPrice;

        LineItemViewHolder(View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvLabel = (TextView) itemView.findViewById(R.id.tv_label);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}
