package com.github.codetanzania.feature.company.billing;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.codetanzania.open311.android.library.utils.DateUtils;

import java.util.List;

import tz.co.codetanzania.R;

import static android.view.View.GONE;

public class SimpleLineItemAdapter extends RecyclerView.Adapter<SimpleLineItemAdapter.LineItemViewHolder> {
    static final int SIMPLE_VIEW_TYPE = 0;

    List<LineItemDisplay> mLineItems;

    SimpleLineItemAdapter(List<LineItemDisplay> lineItems) {
        mLineItems = lineItems;
    }

    @Override
    public LineItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_billing, parent, false);
        return new LineItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LineItemViewHolder holder, int position) {
        LineItemDisplay item = mLineItems.get(position);

        if (item.getLabel() == null) {
            holder.tvLabel.setVisibility(GONE);
        } else {
            holder.tvLabel.setText(item.getLabel());
            holder.tvLabel.setVisibility(View.VISIBLE);
        }

        holder.tvStart.setText(item.getStart());
//        holder.tvStart.setTextColor(item.getNestedColor());

        if (item.getCenter() == null) {
            holder.tvCenter.setVisibility(GONE);
        } else {
            holder.tvCenter.setText(item.getCenter());
            holder.tvCenter.setVisibility(View.VISIBLE);
        }

        holder.tvEnd.setText(item.getEnd());
    }

    @Override
    public int getItemCount() {
        return mLineItems.size();
    }

    static class LineItemViewHolder extends RecyclerView.ViewHolder {
        int type = SIMPLE_VIEW_TYPE;

        TextView tvLabel;
        TextView tvStart;
        TextView tvCenter;
        TextView tvEnd;

        LineItemViewHolder(View itemView) {
            super(itemView);

            tvLabel = (TextView) itemView.findViewById(R.id.tv_label);
            tvStart = (TextView) itemView.findViewById(R.id.tv_start);
            tvCenter = (TextView) itemView.findViewById(R.id.tv_center);
            tvEnd = (TextView) itemView.findViewById(R.id.tv_end);
        }

        int getType() {
            return type;
        }
    }
}
