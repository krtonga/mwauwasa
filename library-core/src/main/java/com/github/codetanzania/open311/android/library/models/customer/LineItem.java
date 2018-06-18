package com.github.codetanzania.open311.android.library.models.customer;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.codetanzania.open311.android.library.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class LineItem implements Parcelable {
    private String name;
    private Integer quantity;
    private String unit;
    private Calendar time;
    private Float price;
    private ArrayList<LineItem> items;

    public LineItem(String name, Integer quantity, String unit,
                       Calendar time, Float price, ArrayList<LineItem> items) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.time = time;
        this.price = price;
        this.items = items;
    }

    private  LineItem(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readInt();
        }
        unit = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readFloat();
        }
        items = in.createTypedArrayList(LineItem.CREATOR);
        time = DateUtils.getCalendarFromParcel(in);
    }

    public static final Creator<LineItem> CREATOR = new Creator<LineItem>() {
        @Override
        public LineItem createFromParcel(Parcel in) {
            return new LineItem(in);
        }

        @Override
        public LineItem[] newArray(int size) {
            return new LineItem[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public ArrayList<LineItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<LineItem> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(quantity);
        }
        dest.writeString(unit);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(price);
        }
        dest.writeTypedList(items);
        DateUtils.setCalendarInParcel(dest, time);
    }
}
