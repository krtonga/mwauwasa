package com.github.codetanzania.core.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class Priority implements Parcelable, Comparable<Priority> {

    private static final int DEFAULT_LOWEST_PRECEDENCE = -9999;

    public static final String WEIGHT = "weight";
    public static final String NAME   = "name";
    public static final String COLOR  = "color";

    private float weight;
    private String name;
    private String color;

    public Priority(float weight, String name, String color) {
        this.weight = weight;
        this.name   = name;
        this.color  = color;
    }

    protected Priority(Parcel in) {
        weight = in.readFloat();
        name = in.readString();
        color = in.readString();
    }

    public static Priority fromJsonObject(JSONObject jsonStr) throws JSONException {
        return (new Priority((float)jsonStr.getDouble(WEIGHT),
                jsonStr.getString(NAME),
                jsonStr.getString(COLOR)));
    }

    /**
     * This fix is here so as to enforce every open311-services received back
     * from the server contains a priority. This is a requirement but unfortunately,
     * the API does not full-fill it. So for every open311-service that misses this
     * part, we are going to create a default priority whose weight is set to -9999 to ensure
     * that it get the lowest precedence
     */
    public static Priority getDefaultPriority() {
        return new Priority(DEFAULT_LOWEST_PRECEDENCE, UUID.randomUUID().toString(), "#000000");
    }

    public static final Creator<Priority> CREATOR = new Creator<Priority>() {
        @Override
        public Priority createFromParcel(Parcel in) {
            return new Priority(in);
        }

        @Override
        public Priority[] newArray(int size) {
            return new Priority[size];
        }
    };

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(weight);
        dest.writeString(name);
        dest.writeString(color);
    }

    @Override
    public String toString() {
        return "Priority{" +
                "weight=" + weight +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Priority o) {
        return weight > o.weight ? -1 : weight < o.weight ? 1 : 0;
    }
}

