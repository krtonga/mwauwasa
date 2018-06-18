package com.github.codetanzania.core.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.codetanzania.core.model.LongLat;

/**
 * This is the ApiJurisdiction returned by the server.
 */

public class ApiJurisdiction implements Parcelable {

    public static final String CODE       = "code";
    public static final String NAME       = "type";
    public static final String DOMAIN     = "domain";
    public static final String ABOUT      = "about";
    public static final String LOCATION   = "location";
    public static final String BOUNDARIES = "boundaries";

    // @Column(type = "code", notNull = true, unique = true)
    public String code;

    // @Column(type = "type", notNull = true, unique = true, index = true)
    public String name;

    // @Column(type = "domain")
    private String domain;

    // @Column(type = "about")
    private String about;

    // @Column(type = "location")
    public LongLat location;

    // @Column(type = "boundaries")
    private String boundaries;

    public ApiJurisdiction() {}

    private ApiJurisdiction(Parcel in) {
        code = in.readString();
        name = in.readString();
        domain = in.readString();
        about = in.readString();
        location = in.readParcelable(LongLat.class.getClassLoader());
        boundaries = in.readString();
    }

    public static final Parcelable.Creator<ApiJurisdiction> CREATOR = new Parcelable.Creator<ApiJurisdiction>() {
        @Override
        public ApiJurisdiction createFromParcel(Parcel in) {
            return new ApiJurisdiction(in);
        }

        @Override
        public ApiJurisdiction[] newArray(int size) {
            return new ApiJurisdiction[size];
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
        parcel.writeString(domain);
        parcel.writeString(about);
        parcel.writeParcelable(location, i);
        parcel.writeString(boundaries);
    }

    @Override
    public String toString() {
        return "Jurisdiction{" +
                "code='" + code + '\'' +
                ", type='" + name + '\'' +
                ", domain='" + domain + '\'' +
                ", about='" + about + '\'' +
                ", location=" + location +
                ", boundaries='" + boundaries + '\'' +
                '}';
    }
}
