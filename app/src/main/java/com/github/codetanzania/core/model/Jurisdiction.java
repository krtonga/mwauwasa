package com.github.codetanzania.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Jurisdiction implements Parcelable {

    private String code;
    private String name;
    private String phone;
    private Phone[] additionalPhones;
    private String email;
    private String domain;
    private String about;
    private String address;
    private String poBox;
    private Location location;
    private String color;
    private String _id;
    private String uri;
    private boolean hq = false;

    public Jurisdiction() {}

    private Jurisdiction(Parcel in) {
        code = in.readString();
        name = in.readString();
        phone = in.readString();
        additionalPhones = (Phone[]) in.readParcelableArray(Phone.class.getClassLoader());
        email = in.readString();
        domain = in.readString();
        about = in.readString();
        address = in.readString();
        poBox = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        color = in.readString();
        _id = in.readString();
        uri = in.readString();
        hq  = in.readInt() == 1;
    }

    public static final Creator<Jurisdiction> CREATOR = new Creator<Jurisdiction>() {
        @Override
        public Jurisdiction createFromParcel(Parcel in) {
            return new Jurisdiction(in);
        }

        @Override
        public Jurisdiction[] newArray(int size) {
            return new Jurisdiction[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Phone[] getAdditionalPhones() {
        return additionalPhones;
    }

    public void setAdditionalPhones(Phone[] additionalPhones) {
        this.additionalPhones = additionalPhones;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPoBox() {
        return poBox;
    }

    public void setPoBox(String poBox) {
        this.poBox = poBox;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isHQ() {
        return hq;
    }

    public void setHQ(boolean hq) {
        this.hq = hq;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeParcelableArray(additionalPhones, flags);
        dest.writeString(email);
        dest.writeString(domain);
        dest.writeString(about);
        dest.writeString(address);
        dest.writeString(poBox);
        dest.writeParcelable(location, flags);
        dest.writeString(color);
        dest.writeString(_id);
        dest.writeString(uri);
        dest.writeInt(hq ? 1 : 0);
    }

    public static class Phone implements Parcelable {
        public String phone;
        public String description;

        protected Phone(Parcel in) {
            phone = in.readString();
            description = in.readString();
        }

        public static final Creator<Phone> CREATOR = new Creator<Phone>() {
            @Override
            public Phone createFromParcel(Parcel in) {
                return new Phone(in);
            }

            @Override
            public Phone[] newArray(int size) {
                return new Phone[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(phone);
            parcel.writeString(description);
        }
    }
}
