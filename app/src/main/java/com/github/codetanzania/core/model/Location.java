package com.github.codetanzania.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {

    private String type;
    private float[] coordinates = new float[2];

    public Location() {}

    protected Location(Parcel in) {
        type = in.readString();
        coordinates = in.createFloatArray();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(float[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeFloatArray(coordinates);
    }
}
