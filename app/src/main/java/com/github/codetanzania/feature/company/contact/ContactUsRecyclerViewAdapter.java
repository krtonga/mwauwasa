package com.github.codetanzania.feature.company.contact;


import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.codetanzania.core.model.Jurisdiction;
import com.github.codetanzania.core.model.Location;

import java.util.List;

import tz.co.codetanzania.R;

import static android.view.View.GONE;
import static com.github.codetanzania.feature.company.contact.ContactUsFragment.ACTION_TYPE_EMAIL;
import static com.github.codetanzania.feature.company.contact.ContactUsFragment.ACTION_TYPE_GET_DIRECTIONS;
import static com.github.codetanzania.feature.company.contact.ContactUsFragment.ACTION_VISIT_WEBSITE;

public class ContactUsRecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_JURISDICTION_PHY_ADDRESS = 0;
    private static final int VIEW_TYPE_JURISDICTION_FULL_DETAILS = 1;

    private final List<Jurisdiction> mJurisdictions;

    private final OnHandleUserAction mOnHandleUserAction;

    ContactUsRecyclerViewAdapter(
            List<Jurisdiction> jurisdictions, OnHandleUserAction onHandleUserAction) {
        mJurisdictions = jurisdictions;
        mOnHandleUserAction = onHandleUserAction;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        RecyclerView.ViewHolder retVal;

        if (viewType == VIEW_TYPE_JURISDICTION_FULL_DETAILS) {
            retVal = new FullDetailsViewHolder(
                    inflater.inflate(R.layout.card_contact_us_full_details, parent, false), mOnHandleUserAction);
        } else {
            retVal = new PhyAddressViewHolder(
                    inflater.inflate(R.layout.card_contact_us_address_only, parent, false), mOnHandleUserAction);
        }

        return retVal;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Jurisdiction jurisdiction = mJurisdictions.get(position);
        if (jurisdiction.isHQ()) {
            ((FullDetailsViewHolder)holder).bindJurisdictionData(jurisdiction);
        } else {
            ((PhyAddressViewHolder)holder).bindJurisdictionData(jurisdiction);
        }
    }

    @Override
    public int getItemViewType(int position) {
//        return mJurisdictions.get(position).isHQ() ?
//                VIEW_TYPE_JURISDICTION_FULL_DETAILS :
//                VIEW_TYPE_JURISDICTION_PHY_ADDRESS;

        return VIEW_TYPE_JURISDICTION_FULL_DETAILS;
    }

    @Override
    public int getItemCount() {
        return mJurisdictions.size();
    }


    public interface OnHandleUserAction {
        void handleUserAction(int actionType, Bundle userData);
    }

    static class PhyAddressViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Jurisdiction mJurisdiction;

        private TextView tvName;
        private View rlPhyAddress;
        private TextView tvPhyAddress;

        final OnHandleUserAction mOnHandleUserAction;

        PhyAddressViewHolder(View itemView, OnHandleUserAction onHandleUserAction) {
            super(itemView);
            rlPhyAddress = itemView.findViewById(R.id.rl_JurisdictionPhyAddress);

            tvPhyAddress = (TextView) itemView.findViewById(R.id.tv_JurisdictionBoxPhyAddressContent);
            tvName = (TextView) itemView.findViewById(R.id.tv_JurisdictionName);

            mOnHandleUserAction = onHandleUserAction;
        }

        void bindJurisdictionData(Jurisdiction jurisdiction) {
            mJurisdiction = jurisdiction;

            tvName.setText(jurisdiction.getName());
            setTextOrHide(rlPhyAddress, tvPhyAddress, jurisdiction.getAddress());
            rlPhyAddress.setOnClickListener(this);
        }

        static void setTextOrHide(View layout, TextView view, String text) {
            if (text == null || TextUtils.isEmpty(text)) {
                layout.setVisibility(GONE);
            } else {
                view.setText(text);
                layout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            Uri data = null;
            String key = null;
            int actionType = -1;

            switch (v.getId()) {
                case R.id.rl_JurisdictionEmail:
                    if (!TextUtils.isEmpty(mJurisdiction.getEmail())) {
//                        data = Uri.fromParts(
//                                "mailto", mJurisdiction.getEmail(), null);
//                        key = ContactUtils.EMAIL_DATA;
                        actionType = ACTION_TYPE_EMAIL;

                        Bundle resBundle = new Bundle();
//                        resBundle.putParcelable(key, data);
                        resBundle.putString(ContactUtils.EMAIL_DATA, mJurisdiction.getEmail());
                        mOnHandleUserAction.handleUserAction(actionType, resBundle);
                    }
                    break;
                case R.id.rl_JurisdictionPhyAddress:
                    Location location = mJurisdiction.getLocation();
                    if (location != null && location.getCoordinates().length == 2) {
                        data = Uri.parse("google.navigation:q="+
                                location.getCoordinates()[0]+","+location.getCoordinates()[1]);
                    } else if (!TextUtils.isEmpty(mJurisdiction.getAddress())) {
                        data = Uri.parse("google.navigation:q="+mJurisdiction.getAddress());
                    }
                    if (data != null) {
                        key = ContactUtils.URI_DATA;
                        actionType = ACTION_TYPE_GET_DIRECTIONS;
                    }
                    break;
                case R.id.rl_JurisdictionWeb:
                    if (!TextUtils.isEmpty(mJurisdiction.getUri())) {
                        data = Uri.parse("http://" + mJurisdiction.getUri());
                        key = ContactUtils.URI_DATA;
                        actionType = ACTION_VISIT_WEBSITE;
                    }
                default:
                    break;
            }

            if (data != null && !TextUtils.isEmpty(key) && actionType != -1) {
                Bundle resBundle = new Bundle();
                resBundle.putParcelable(key, data);
                mOnHandleUserAction.handleUserAction(actionType, resBundle);
            }
        }
    }

    private static class FullDetailsViewHolder extends PhyAddressViewHolder {

        private TextView tvAbout;

        private RecyclerView rvPhone;

        private View rlMail;
        private View rlEmail;
        private View rlWebsite;

        private TextView tvMailContent;
        private TextView tvEmailContent;
        private TextView tvWebAddrContent;

        FullDetailsViewHolder(View itemView, OnHandleUserAction onHandleUserAction) {
            super(itemView, onHandleUserAction);

            rvPhone = (RecyclerView) itemView.findViewById(R.id.rv_JurisdictionPhones);

            rlMail = itemView.findViewById(R.id.rl_mailBox);
            rlEmail = itemView.findViewById(R.id.rl_JurisdictionEmail);
            rlWebsite = itemView.findViewById(R.id.rl_JurisdictionWeb);

            tvAbout = (TextView) itemView.findViewById(R.id.tv_JurisdictionAbout);
            tvMailContent = (TextView) itemView.findViewById(R.id.tv_mailBoxContent);
            tvEmailContent = (TextView) itemView.findViewById(R.id.tv_JurisdictionEmailContent);
            tvWebAddrContent = (TextView) itemView.findViewById(R.id.tv_JurisdictionWebContent);
        }

        @Override
        void bindJurisdictionData(Jurisdiction jurisdiction) {
            super.bindJurisdictionData(jurisdiction);

            setTextOrHide(tvAbout, tvAbout, jurisdiction.getAbout());
            setTextOrHide(rlMail, tvMailContent, jurisdiction.getPoBox());
            setTextOrHide(rlEmail, tvEmailContent, jurisdiction.getEmail());
            setTextOrHide(rlWebsite, tvWebAddrContent, jurisdiction.getUri());

            setPhones(jurisdiction);
            rlWebsite.setOnClickListener(this);
            rlEmail.setOnClickListener(this);
        }

        private void setPhones(Jurisdiction jurisdiction) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                    itemView.getContext(), LinearLayoutManager.VERTICAL, false);
            PhoneAdapter jurisdictionsRecyclerViewAdapter =
                    new PhoneAdapter(jurisdiction, mOnHandleUserAction);
            rvPhone.setAdapter(jurisdictionsRecyclerViewAdapter);
            rvPhone.setLayoutManager(linearLayoutManager);
            rvPhone.setHasFixedSize(true);
        }
    }

}
