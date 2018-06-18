package com.github.codetanzania.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LongLat implements Parcelable {
    private Double longitudes;
    private Double latitudes;

    public LongLat() {}

    public LongLat(Double longitudes, Double latitudes) {
        this.setLongitudes(longitudes);
        this.setLatitudes(latitudes);
    }

    protected LongLat(Parcel in) {
        this(in.readDouble(), in.readDouble());
    }

    public static final Creator<LongLat> CREATOR = new Creator<LongLat>() {
        @Override
        public LongLat createFromParcel(Parcel in) {
            return new LongLat(in);
        }

        @Override
        public LongLat[] newArray(int size) {
            return new LongLat[size];
        }
    };

    public Double getLongitudes() {
        return longitudes;
    }

    public final void setLongitudes(Double longitudes) {
        this.longitudes = longitudes;
    }

    public Double getLatitudes() {
        return latitudes;
    }

    public final void setLatitudes(Double latitudes) {
        this.latitudes = latitudes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(longitudes);
        parcel.writeDouble(latitudes);
    }

    @Override
    public String toString() {
        return "LongLat{" +
                "longitudes=" + longitudes +
                ", latitudes=" + latitudes +
                '}';
    }
}
