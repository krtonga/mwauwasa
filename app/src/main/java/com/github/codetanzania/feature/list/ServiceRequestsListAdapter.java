package com.github.codetanzania.feature.list;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.codetanzania.open311.android.library.models.Problem;
import com.github.codetanzania.util.LocalTimeUtils;

import java.util.List;

import tz.co.codetanzania.R;

public class ServiceRequestsListAdapter extends ClickAwareRecyclerViewAdapter<Problem> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    /* A list of open311Service requests by the civilian */
    private final List<Problem> mServiceRequests;

    /* Title of the issues */
    private final String mTitle;

    /* context allows us to access to resources as in ordinary first class citizen components */
    private Context mContext;

    /* constructor */
    public ServiceRequestsListAdapter(
            Context mContext,
            String title,
            List<Problem> serviceRequests,
            OnItemClickListener<Problem> onItemClickListener) {

        super(onItemClickListener);
        this.mContext = mContext;
        this.mTitle = title;
        this.mServiceRequests = serviceRequests;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /* inflate view and return view holder */
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.cardview_issue_list_item, parent, false);
            return new ServiceRequestViewHolder(view, mClickListener);
        } else if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.issue_ticket_groups_title, parent, false);
            return new ServiceHeaderViewHolder(view);
        }

        throw new UnsupportedOperationException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        /* bind data to the views */
        if (getItemViewType(position) == TYPE_HEADER) {
            ((ServiceHeaderViewHolder)holder).tvHeader.setText(mTitle);
        } else {
            Problem request = this.mServiceRequests.get(position);

            ServiceRequestViewHolder srViewHolder = (ServiceRequestViewHolder) holder;
            srViewHolder.tvTitle.setText(request.getCategory().getName());
            srViewHolder.tvDescription.setText(request.getDescription());
            srViewHolder.tvServiceTypeIcon.setText(request.getCategory().getCode());

            DrawableCompat.setTint(srViewHolder.tvServiceTypeIcon.getBackground(), getColor(request.getStatus()));
            srViewHolder.tvDateCreated.setText(LocalTimeUtils.formatShortDate(request.getCreatedAt()));

            srViewHolder.tvTicketID.setText(request.getTicketNumber());

            srViewHolder.bind(request, srViewHolder.crdTicketItem);
        }
    }

    private int getColor(com.github.codetanzania.open311.android.library.models.Status status) {
        try {
            return Color.parseColor(status.getColor());
        } catch (IllegalArgumentException e) {
            return ContextCompat.getColor(mContext,
                    "Closed".equals(status.getName()) ? R.color.green : R.color.iconPrimary);
        }
    }

    @Override
    public int getItemViewType(int pos) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        /* Size of the requests */
        return mServiceRequests.size();
    }

    private static class ServiceRequestViewHolder extends RecyclerView.ViewHolder {

        TextView tvServiceTypeIcon;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvDateCreated;
        // TextView tvStatus;
        TextView tvTicketID;
        View     crdTicketItem;

        private OnItemClickListener<Problem> mClickListener;

        ServiceRequestViewHolder(View itemView, OnItemClickListener<Problem> mClickListener) {
            super(itemView);

            this.mClickListener = mClickListener;

            tvServiceTypeIcon = (TextView) itemView.findViewById(R.id.tv_serviceTypeIcon);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_serviceReqTitle);
            tvDateCreated = (TextView) itemView.findViewById(R.id.tv_serviceReqDate);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_serviceReqDescription);
            // tvStatus = (TextView) itemView.findViewById(R.id.tv_Status);
            // vwStatusView = itemView.findViewById(R.id.vw_serviceReqStatus);
            crdTicketItem = itemView.findViewById(R.id.crd_TicketItem);
            tvTicketID = (TextView) itemView.findViewById(R.id.tv_TicketID);
        }

        void bind(final Problem request, View view) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(request);
                }
            });
        }
    }

    private static class ServiceHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tvHeader;

        ServiceHeaderViewHolder (View itemView) {
            super(itemView);
            tvHeader = (TextView) itemView.findViewById(R.id.tv_serviceReqHeaderName);
        }
    }

}
