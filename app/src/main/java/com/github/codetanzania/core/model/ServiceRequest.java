package com.github.codetanzania.core.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.github.codetanzania.open311.android.library.models.Reporter;
import com.github.codetanzania.util.Util;

import java.util.Date;
import java.util.List;

public class ServiceRequest implements Parcelable {

    private static final String TAG = "ServiceRequest";

    public String id;
    public String code;

    public String description;

    public Reporter reporter;

    public Service service;

    public Priority priority;

    public String jurisdiction;

    public String address;
    public Float longitude;
    public Float latitude;

    public Status status;

    public Date createdAt;
    public Date updatedAt;
    public Date resolvedAt;

    private List<Attachment>  attachments;
    public List<Comment> comments;

    public ServiceRequest(){}

    public boolean hasPhotoAttachment() {
        return attachments != null && !attachments.isEmpty() &&
                !Util.isNull(attachments.get(0)) &&
                !TextUtils.isEmpty(attachments.get(0).getContent());
    }

    public void setImageUri(String uri) {
        attachments.get(0).setContent(uri);
    }

    public Attachment getAttachment(int index) {
        if (hasPhotoAttachment()) {
            return attachments.get(index);
        }
        return null;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Uri getImageUri() {
        if (hasPhotoAttachment()) {
            return Uri.parse(attachments.get(0).getContent());
        }
        return null;

    }

    public ServiceRequest(Parcel in) {
        id = in.readString();
        code = in.readString();
        description = in.readString();
        jurisdiction = in.readString();
        service = in.readParcelable(Service.class.getClassLoader());
        reporter = in.readParcelable(Reporter.class.getClassLoader());
        priority = in.readParcelable(Priority.class.getClassLoader());
        address = in.readString();
        longitude = in.readFloat();
        latitude = in.readFloat();
        attachments = in.createTypedArrayList(Attachment.CREATOR);
        comments = in.createTypedArrayList(Comment.CREATOR);
        status = in.readParcelable(Status.class.getClassLoader());
        createdAt = Util.extractDateFromParcel(in);
        updatedAt = Util.extractDateFromParcel(in);
        resolvedAt = Util.extractDateFromParcel(in);
    }

    public static final Creator<ServiceRequest> CREATOR = new Creator<ServiceRequest>() {
        @Override
        public ServiceRequest createFromParcel(Parcel in) {
            return new ServiceRequest(in);
        }

        @Override
        public ServiceRequest[] newArray(int size) {
            return new ServiceRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(code);
        parcel.writeString(description);
        parcel.writeString(jurisdiction);
        parcel.writeParcelable(service, i);
        parcel.writeParcelable(reporter, i);
        parcel.writeParcelable(priority, i);
        parcel.writeString(address);
        parcel.writeFloat(longitude);
        parcel.writeFloat(latitude);
        parcel.writeTypedList(attachments);
        parcel.writeTypedList(comments);
        parcel.writeParcelable(status, i);
        Util.addDateToParcel(parcel, createdAt);
        Util.addDateToParcel(parcel, updatedAt);
        Util.addDateToParcel(parcel, resolvedAt);
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
            "menu_jurisdiction=" + jurisdiction +
            ", service=" + service +
            ", reporter=" + reporter +
            ", priority=" + priority +
            ", address='" + address + '\'' +
            ", longitude='" + longitude + '\'' +
            ", latitude='" + latitude + '\'' +
            ", status=" + status +
            ", attachments=" + attachments +
            ", comments=" + comments +
            ", resolvedAt=" + resolvedAt +
            ", code=" + code +
        '}';
    }
}