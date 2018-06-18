package com.github.codetanzania.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.codetanzania.util.Util;

import java.util.Date;

public class Attachment implements Parcelable {

    private Date createdAt;
    private Date updatedAt;
    private String content;
    private String mime;

    /* default constructor for creating */
    public Attachment() {}

    protected Attachment(Parcel in) {
        content   = in.readString();
        mime      = in.readString();
        createdAt = Util.extractDateFromParcel(in);
        updatedAt = Util.extractDateFromParcel(in);
    }

    public static final Creator<Attachment> CREATOR = new Creator<Attachment>() {
        @Override
        public Attachment createFromParcel(Parcel in) {
            return new Attachment(in);
        }

        @Override
        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(mime);
        Util.addDateToParcel(dest, createdAt);
        Util.addDateToParcel(dest, updatedAt);
    }
}
