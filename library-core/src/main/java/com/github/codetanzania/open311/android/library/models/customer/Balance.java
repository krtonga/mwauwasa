package com.github.codetanzania.open311.android.library.models.customer;

import android.os.Parcel;
import android.os.Parcelable;

public class Balance implements Parcelable {
    private Float outstanding;
    private Float open;
    private Float charges;
    private Float debt;
    private Float close;

    public Balance(Float outstanding, Float open, Float charges,
                   Float debt, Float close) {
        this.outstanding = outstanding;
        this.open = open;
        this.charges = charges;
        this.debt = debt;
        this.close = close;
    }

    private  Balance(Parcel in) {
        if (in.readByte() == 0) {
            outstanding = null;
        } else {
            outstanding = in.readFloat();
        }
        if (in.readByte() == 0) {
            open = null;
        } else {
            open = in.readFloat();
        }
        if (in.readByte() == 0) {
            charges = null;
        } else {
            charges = in.readFloat();
        }
        if (in.readByte() == 0) {
            debt = null;
        } else {
            debt = in.readFloat();
        }
        if (in.readByte() == 0) {
            close = null;
        } else {
            close = in.readFloat();
        }
    }

    public static final Creator<Balance> CREATOR = new Creator<Balance>() {
        @Override
        public Balance createFromParcel(Parcel in) {
            return new Balance(in);
        }

        @Override
        public Balance[] newArray(int size) {
            return new Balance[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (outstanding == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(outstanding);
        }
        if (open == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(open);
        }
        if (charges == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(charges);
        }
        if (debt == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(debt);
        }
        if (close == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(close);
        }
    }

    public Float getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(Float outstanding) {
        this.outstanding = outstanding;
    }

    public Float getOpen() {
        return open;
    }

    public void setOpen(Float open) {
        this.open = open;
    }

    public Float getCharges() {
        return charges;
    }

    public void setCharges(Float charges) {
        this.charges = charges;
    }

    public Float getDebt() {
        return debt;
    }

    public void setDebt(Float debt) {
        this.debt = debt;
    }

    public Float getClose() {
        return close;
    }

    public void setClose(Float close) {
        this.close = close;
    }
}
