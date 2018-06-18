package com.github.codetanzania.open311.android.library.models.customer;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.codetanzania.open311.android.library.utils.DateUtils;

import java.util.Calendar;

public class Accessor implements Parcelable {
    private String locale;
    private String name;
    private String phone;
    private String email;
    private Calendar verifiedAt;
    private Calendar createdAt;
    private Calendar updatedAt;

    public Accessor(String locale, String name, String phone, String email,
                    Calendar verifiedAt, Calendar createdAt, Calendar updatedAt) {
        this.locale = locale;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.verifiedAt = verifiedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private  Accessor(Parcel in) {
        locale = in.readString();
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        verifiedAt = DateUtils.getCalendarFromParcel(in);
        createdAt = DateUtils.getCalendarFromParcel(in);
        updatedAt = DateUtils.getCalendarFromParcel(in);
    }

    public static final Creator<Accessor> CREATOR = new Creator<Accessor>() {
        @Override
        public Accessor createFromParcel(Parcel in) {
            return new Accessor(in);
        }

        @Override
        public Accessor[] newArray(int size) {
            return new Accessor[size];
        }
    };

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Calendar getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Calendar verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Calendar updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locale);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        DateUtils.setCalendarInParcel(dest, verifiedAt);
        DateUtils.setCalendarInParcel(dest, createdAt);
        DateUtils.setCalendarInParcel(dest, updatedAt);
    }
}
