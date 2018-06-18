package com.github.codetanzania.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.codetanzania.core.api.model.ApiJurisdiction;
import com.github.codetanzania.core.api.model.Open311Service;

import java.util.List;

// @Table(name = "service_group", _id = BaseColumns._ID)
public class ServiceGroup implements Parcelable {

    // @Column(name = "name")
    public String name;

    // @Column(name = "menu_jurisdiction")
    private ApiJurisdiction jurisdiction;

    // @Column(name = "open311Services")
    private List<Open311Service> open311Services;

    public ServiceGroup() {}

    private ServiceGroup(Parcel in) {
        name = in.readString();
        jurisdiction = in.readParcelable(ApiJurisdiction.class.getClassLoader());
        open311Services = in.createTypedArrayList(Open311Service.CREATOR);
    }

    public static final Creator<ServiceGroup> CREATOR = new Creator<ServiceGroup>() {
        @Override
        public ServiceGroup createFromParcel(Parcel in) {
            return new ServiceGroup(in);
        }

        @Override
        public ServiceGroup[] newArray(int size) {
            return new ServiceGroup[size];
        }
    };

    public List<Open311Service> getOpen311Services() {
        return open311Services;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeParcelable(jurisdiction, i);
        parcel.writeTypedList(open311Services);
    }

    @Override
    public String toString() {
        return "ServiceGroup{" +
                "name='" + name + '\'' +
                ", menu_jurisdiction=" + jurisdiction +
                ", open311Services=" + open311Services +
                '}';
    }
}
