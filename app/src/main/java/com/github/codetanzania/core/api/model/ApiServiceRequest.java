package com.github.codetanzania.core.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.github.codetanzania.core.model.Attachment;
import com.github.codetanzania.core.model.Comment;
import com.github.codetanzania.core.model.Priority;
import com.github.codetanzania.core.model.ServiceRequest;
import com.github.codetanzania.open311.android.library.models.Reporter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This is the service request object returned from the Company server.
 */

public class ApiServiceRequest implements Parcelable {

    private static final String TAG = "ApiServiceRequest";

    public ApiServiceRequest(Parcel in) {
        _id = in.readString();
        code = in.readString();
        description = in.readString();
        jurisdiction = in.readParcelable(ApiJurisdiction.class.getClassLoader());
        service = in.readParcelable(Open311Service.class.getClassLoader());
        reporter = in.readParcelable(Reporter.class.getClassLoader());
        priority = in.readParcelable(Priority.class.getClassLoader());
        address = in.readString();
        longitude = in.readFloat();
        latitude = in.readFloat();
        attachments = in.createTypedArrayList(Attachment.CREATOR);
        comments = in.createTypedArrayList(Comment.CREATOR);
        status = in.readParcelable(ApiServiceRequest.Status.class.getClassLoader());
        createdAt = new Date(in.readLong());
        updatedAt = new Date(in.readLong());
        resolvedAt = new Date(in.readLong());
    }

    public static final Parcelable.Creator<ServiceRequest> CREATOR = new Parcelable.Creator<ServiceRequest>() {
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
        parcel.writeString(_id);
        parcel.writeString(code);
        parcel.writeString(description);
        parcel.writeParcelable(jurisdiction, i);
        parcel.writeParcelable(service, i);
        parcel.writeParcelable(reporter, i);
        parcel.writeParcelable(priority, i);
        parcel.writeString(address);
        parcel.writeFloat(longitude);
        parcel.writeFloat(latitude);
        parcel.writeTypedList(attachments);
        parcel.writeTypedList(comments);
        parcel.writeParcelable(status, i);
        parcel.writeLong(createdAt.getTime());
        parcel.writeLong(updatedAt.getTime());
        if (resolvedAt != null) {
            parcel.writeLong(resolvedAt.getTime());
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class Status implements Parcelable {
        public String name;
        float weight;
        public String color;
        Date updatedAt;
        //@NonNull
        Date createdAt;

        Status(Parcel in) {
            name = in.readString();
            weight = in.readFloat();
            color = in.readString();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
                updatedAt = sdf.parse(in.readString());
                createdAt = sdf.parse(in.readString());
            } catch (Exception e) {
                Log.e(TAG, "ERROR: " + e.getMessage());
            }
        }

        public static final Creator<ApiServiceRequest.Status> CREATOR = new Creator<ApiServiceRequest.Status>() {
            @Override
            public ApiServiceRequest.Status createFromParcel(Parcel in) {
                return new ApiServiceRequest.Status(in);
            }

            @Override
            public ApiServiceRequest.Status[] newArray(int size) {
                return new ApiServiceRequest.Status[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(name);
            parcel.writeFloat(weight);
            parcel.writeString(color);
            if (updatedAt != null)
                parcel.writeString(updatedAt.toString());
            if (createdAt != null)
                parcel.writeString(createdAt.toString());
        }
    }

    /* public enum Priority {
         LOW, NORMAL, HIGH
     }*/
    public String _id;

    // @Column(type = "menu_jurisdiction")
    public ApiJurisdiction jurisdiction;

    // @Column(type = "open311Service")
    public Open311Service service;

    // @Column(type = "reporter")
    public Reporter reporter;

    // @Column(type = "address")
    public String address;

    // @Column(type = "longitude")
    public Float longitude;

    // @Column(type = "latitude")
    public Float latitude;

    // @Column(type = "status")
    public ApiServiceRequest.Status status;

    public Priority priority;

    public List<Attachment> attachments;
    public List<Comment> comments;

    public Date resolvedAt;

    public Date createdAt;

    public Date updatedAt;

    public String description;

    public String code;

    // take comma separated strings and convert into an array of strings
    // public void setAttachments(String...attachments) {
    //    this.attachments = Arrays.asList(attachments);
    // }

    // List<Comment> getComments() {
    //    return getMany(Comment.class, "comment");
    // }


    @Override
    public String toString() {
        return "ServiceRequest{" +
                "menu_jurisdiction=" + jurisdiction +
                ", open311Service=" + service +
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
