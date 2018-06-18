package com.github.codetanzania.open311.android.library.models.customer;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.codetanzania.open311.android.library.utils.DateUtils;

import java.util.Calendar;

public class BillingPeriod implements Parcelable {
    private Calendar billedAt;
    private Calendar startedAt;
    private Calendar endedAt;
    private Calendar duedAt;

    public BillingPeriod(Calendar billedAt, Calendar startedAt,
                            Calendar endedAt, Calendar duedAt) {
        this.billedAt = billedAt;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.duedAt = duedAt;
    }

    private BillingPeriod(Parcel in) {
        billedAt = DateUtils.getCalendarFromParcel(in);
        startedAt = DateUtils.getCalendarFromParcel(in);
        endedAt = DateUtils.getCalendarFromParcel(in);
        duedAt = DateUtils.getCalendarFromParcel(in);
    }

    public static final Creator<BillingPeriod> CREATOR = new Creator<BillingPeriod>() {
        @Override
        public BillingPeriod createFromParcel(Parcel in) {
            return new BillingPeriod(in);
        }

        @Override
        public BillingPeriod[] newArray(int size) {
            return new BillingPeriod[size];
        }
    };

    public Calendar getBilledAt() {
        return billedAt;
    }

    public void setBilledAt(Calendar billedAt) {
        this.billedAt = billedAt;
    }

    public Calendar getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Calendar startedAt) {
        this.startedAt = startedAt;
    }

    public Calendar getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Calendar endedAt) {
        this.endedAt = endedAt;
    }

    public Calendar getDuedAt() {
        return duedAt;
    }

    public void setDuedAt(Calendar duedAt) {
        this.duedAt = duedAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DateUtils.setCalendarInParcel(dest, billedAt);
        DateUtils.setCalendarInParcel(dest, startedAt);
        DateUtils.setCalendarInParcel(dest, endedAt);
        DateUtils.setCalendarInParcel(dest, duedAt);
    }
}
