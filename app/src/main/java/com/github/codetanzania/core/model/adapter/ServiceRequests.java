package com.github.codetanzania.core.model.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.codetanzania.core.model.ServiceRequest;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ServiceRequests implements Parcelable {

    @SerializedName("servicerequests")
    public List<ServiceRequest> requests;

    public ServiceRequests(Parcel in) {
        requests = in.createTypedArrayList(ServiceRequest.CREATOR);
    }

    public ServiceRequests() {}

    public static final Creator<ServiceRequests> CREATOR = new Creator<ServiceRequests>() {
        @Override
        public ServiceRequests createFromParcel(Parcel in) {
            return new ServiceRequests(in);
        }

        @Override
        public ServiceRequests[] newArray(int size) {
            return new ServiceRequests[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(requests);
    }
}
