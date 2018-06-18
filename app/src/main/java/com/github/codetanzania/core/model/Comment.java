package com.github.codetanzania.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.codetanzania.core.api.model.Open311Service;

import java.util.Date;

// @Table(name = "comment", _id = BaseColumns._ID)
public class Comment implements Parcelable {

    public static final String SERVICE = "open311Service";
    public static final String COMMENTER = "commenter";
    public static final String TIMESTAMP = "timestamp";
    public static final String CONTENT = "content";

    // @Column(name = "open311Service", notNull = true)
    private Open311Service open311Service;

    // @Column(name = "commentor", notNull = true)
    public String  commentor;

    // @Column(name = "tsp", notNull = true)
    public Date    timestamp;

    // @Column(name = "content", notNull = true)
    public String  content;

    public Comment() {}

    private Comment(Parcel in) {
        commentor = in.readString();
        content = in.readString();
        timestamp = new Date(in.readLong());
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(commentor);
        parcel.writeString(content);
        parcel.writeLong(timestamp.getTime());
    }

    @Override
    public String toString() {
        return "Comment{" +
                "open311Service=" + open311Service +
                ", commentor='" + commentor + '\'' +
                ", timestamp=" + timestamp +
                ", content='" + content + '\'' +
                '}';
    }
}
