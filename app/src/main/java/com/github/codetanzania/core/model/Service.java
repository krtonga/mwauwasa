package com.github.codetanzania.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import tz.co.codetanzania.R;

/**
 * This is the type of service offered.
 */

public class Service implements Parcelable {
    public String code;
    public String name;
    public String color;

    public Service(String code, String name, String color) {
        this.code = code;
        this.name = name;
        this.color = color;
    }

    public int getIcon() {
        System.out.println("Icon res: name:"+name+" code: "+code);
        switch (code) {
            case "B" : // Billing
                return R.drawable.ic_category_water_impurity;
            case "LK" : // Leakage
                return R.drawable.ic_category_leakage;
            case "LW" : // Lack of Water
                return R.drawable.ic_category_disconnection;
            case "C" : // Clarification
                return R.drawable.ic_category_maintanance;
            case "0" : // Other
                return R.drawable.ic_category_maintanance;
            default :
                return R.drawable.ic_category_maintanance;
        }
    }

    private Service(Parcel in) {
        code = in.readString();
        name = in.readString();
        color = in.readString();
    }

    public static final Parcelable.Creator<Service> CREATOR =
            new Parcelable.Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        parcel.writeString(name);
        parcel.writeString(color);
    }

    @Override
    public String toString() {
        return "Service{" +
                "code='" + code + '\'' +
                ", type='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
