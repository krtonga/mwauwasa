package com.github.codetanzania.core.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Tariff implements Parcelable {

    private transient static final String TAG = "Tariff";

    @SerializedName("service_type")
    private String serviceType;

    @SerializedName("levels")
    private Level[] levels;

    public String getServiceType() {
        return serviceType;
    }

    public Level[] getLevels() {
        return levels;
    }

    protected Tariff(Parcel in) {
        serviceType = in.readString();
        levels = in.createTypedArray(Level.CREATOR);
    }

    public static final Creator<Tariff> CREATOR = new Creator<Tariff>() {
        @Override
        public Tariff createFromParcel(Parcel in) {
            return new Tariff(in);
        }

        @Override
        public Tariff[] newArray(int size) {
            return new Tariff[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(serviceType);
        parcel.writeTypedArray(levels, i);
    }

    public static class Level implements Parcelable {
        @SerializedName("customer_type")
        private String customerType;
        @SerializedName("quantity")
        private String quantity;
        @SerializedName("tariff")
        private String cost;

        public String getCustomerType() {
            return customerType;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getCost() {
            return cost;
        }

        protected Level(Parcel in) {
            customerType = in.readString();
            quantity = in.readString();
            cost = in.readString();
        }

        public static final Creator<Level> CREATOR = new Creator<Level>() {
            @Override
            public Level createFromParcel(Parcel in) {
                return new Level(in);
            }

            @Override
            public Level[] newArray(int size) {
                return new Level[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(customerType);
            parcel.writeString(quantity);
            parcel.writeString(cost);
        }
    }
}
