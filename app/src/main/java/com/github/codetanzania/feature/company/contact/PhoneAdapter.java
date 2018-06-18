package com.github.codetanzania.feature.company.contact;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.codetanzania.core.model.Jurisdiction;

import tz.co.codetanzania.R;

import static com.github.codetanzania.feature.company.contact.ContactUsFragment.ACTION_TYPE_DIAL;


// This is used in contact views to show a list of phone items on the issue contact items.
// In the future, it is a candidate for refactoring.

class PhoneAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Jurisdiction mJurisdiction;
    private final ContactUsRecyclerViewAdapter.OnHandleUserAction mOnHandleUserAction;

    PhoneAdapter(Jurisdiction jurisdiction, ContactUsRecyclerViewAdapter.OnHandleUserAction actionHandler) {
        mJurisdiction = jurisdiction;
        mOnHandleUserAction = actionHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new PhoneViewHolder(
                inflater.inflate(R.layout.item_contact_us_phone, parent, false),
                mOnHandleUserAction);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String visible = null;
        String number = null;
        if (position == 0 && !TextUtils.isEmpty(mJurisdiction.getPhone())) {
            visible = mJurisdiction.getPhone();
            number = mJurisdiction.getPhone();
        } else if (mJurisdiction.getAdditionalPhones() != null) {
            if (position == 0) {
                visible = mJurisdiction.getAdditionalPhones()[0].description;
                number = mJurisdiction.getAdditionalPhones()[0].phone;
            } else {
                visible = mJurisdiction.getAdditionalPhones()[position-1].description;
                number = mJurisdiction.getAdditionalPhones()[position-1].phone;
            }
        }
        ((PhoneViewHolder) holder).bindPhoneData(visible, number);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (!TextUtils.isEmpty(mJurisdiction.getPhone())) {
            count++;
        }
        if (mJurisdiction.getAdditionalPhones() != null) {
            count = count+mJurisdiction.getAdditionalPhones().length;
        }
        return count;
    }

    static class PhoneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;
        TextView tvPhoneVisible;
        String mPhone;

        final ContactUsRecyclerViewAdapter.OnHandleUserAction mOnHandleUserAction;

        PhoneViewHolder(View itemView, ContactUsRecyclerViewAdapter.OnHandleUserAction onHandleUserAction) {
            super(itemView);
            this.itemView = itemView;
            mOnHandleUserAction = onHandleUserAction;

            tvPhoneVisible = (TextView) itemView.findViewById(R.id.tv_JurisdictionPhoneContent);
        }

        void bindPhoneData(String visiblePhone, String phoneNumber) {
            tvPhoneVisible.setText(visiblePhone);
            mPhone = phoneNumber;

            if (TextUtils.isEmpty(mPhone)) {
                tvPhoneVisible.setTextColor(
                        itemView.getContext().getResources().getColor(R.color.colorHeavyGray));
            } else {
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v){
            Uri data = Uri.parse("tel:" + mPhone);

            if (data != null
                    && mOnHandleUserAction != null) {
                Bundle resBundle = new Bundle();
                resBundle.putParcelable(ContactUtils.TELEPHONE_DATA, data);
                mOnHandleUserAction.handleUserAction(ACTION_TYPE_DIAL, resBundle);
            }
        }
    }
}
