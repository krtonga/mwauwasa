package com.github.codetanzania.open311.android.library.models.customer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Bill implements Parcelable {
    private ArrayList<LineItem> items;
    private String number;
    private BillingPeriod period;
    private Balance balance;
    private String currency;
    private String notes;

    public Bill(ArrayList<LineItem> items, String number, BillingPeriod period,
                Balance balance, String currency, String notes) {
        this.items = items;
        this.number = number;
        this.period = period;
        this.balance = balance;
        this.currency = currency;
        this.notes = notes;
    }

    private Bill(Parcel in) {
        items = in.createTypedArrayList(LineItem.CREATOR);
        number = in.readString();
        period = in.readParcelable(BillingPeriod.class.getClassLoader());
        balance = in.readParcelable(Balance.class.getClassLoader());
        currency = in.readString();
        notes = in.readString();
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    public ArrayList<LineItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<LineItem> items) {
        this.items = items;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BillingPeriod getPeriod() {
        return period;
    }

    public void setPeriod(BillingPeriod period) {
        this.period = period;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeString(number);
        dest.writeParcelable(period, flags);
        dest.writeParcelable(balance, flags);
        dest.writeString(currency);
        dest.writeString(notes);
    }
}
