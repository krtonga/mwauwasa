package com.github.codetanzania.open311.android.library.models.customer;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.github.codetanzania.open311.android.library.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class CustomerAccount implements Parcelable {
    private String id;
    private String number;
    private String name;
    private String phone;
    private String email;
    private String neighborhood;
    private String address;
    private String locale;
    private Location location;
    private ArrayList<Accessor> accessors;
    private ArrayList<Bill> bills;
    private Boolean active;
    private Calendar createdAt;
    private Calendar updatedAt;

    public CustomerAccount(String number, String name, String phone, String email,
                           String neighborhood, String address, String locale,
                           Location location, ArrayList<Accessor> accessors,
                           ArrayList<Bill> bills, Boolean active, String _id,
                           Calendar createdAt, Calendar updatedAt){
        this.number = number;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.neighborhood = neighborhood;
        this.address = address;
        this.locale = locale;
        this.location = location;
        this.accessors = accessors;
        this.bills = bills;
        this.active = active;
        this.id = _id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private CustomerAccount(Parcel in) {
        id = in.readString();
        number = in.readString();
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        neighborhood = in.readString();
        address = in.readString();
        locale = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        accessors = in.createTypedArrayList(Accessor.CREATOR);
        bills = in.createTypedArrayList(Bill.CREATOR);
        byte tmpActive = in.readByte();
        active = tmpActive == 0 ? null : tmpActive == 1;
        updatedAt = DateUtils.getCalendarFromParcel(in);
        createdAt = DateUtils.getCalendarFromParcel(in);
    }

    public static final Creator<CustomerAccount> CREATOR = new Creator<CustomerAccount>() {
        @Override
        public CustomerAccount createFromParcel(Parcel in) {
            return new CustomerAccount(in);
        }

        @Override
        public CustomerAccount[] newArray(int size) {
            return new CustomerAccount[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<Accessor> getAccessors() {
        return accessors;
    }

    public void setAccessors(ArrayList<Accessor> accessors) {
        this.accessors = accessors;
    }

    public ArrayList<Bill> getBills() {
        return bills;
    }

    public void setBills(ArrayList<Bill> bills) {
        this.bills = bills;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        dest.writeString(id);
        dest.writeString(number);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(neighborhood);
        dest.writeString(address);
        dest.writeString(locale);
        dest.writeParcelable(location, flags);
        dest.writeTypedList(accessors);
        dest.writeTypedList(bills);
        dest.writeByte((byte) (active == null ? 0 : active ? 1 : 2));
        DateUtils.setCalendarInParcel(dest, updatedAt);
        DateUtils.setCalendarInParcel(dest, createdAt);
    }
}
