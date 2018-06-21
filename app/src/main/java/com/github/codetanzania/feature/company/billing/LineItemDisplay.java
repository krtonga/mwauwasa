package com.github.codetanzania.feature.company.billing;

import com.github.codetanzania.open311.android.library.models.customer.LineItem;

import java.util.List;

import tz.co.codetanzania.R;

public class LineItemDisplay {
    private String mLabel;
    private String mStart;
    private String mCenter;
    private String mEnd;
    private List<LineItemDisplay> mNested;
    private int mNestedColor;

    public LineItemDisplay(String start, String end) {
        this(null, start, null, end);
    }

    public LineItemDisplay(String start, String end, int color) {
        this(null, start, null, end, null, color);
    }

    public LineItemDisplay(String label, String start, String center, String end) {
        this(label, start, center, end, null, R.color.colorAccent);
    }

    public LineItemDisplay(String label, String start, String center, String end,
                           List<LineItemDisplay> nested, int color) {
        mLabel = label;
        mStart = start;
        mCenter = center;
        mEnd = end;
        mNested = nested;
        mNestedColor = color;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }

    public String getStart() {
        return mStart;
    }

    public void setStart(String start) {
        this.mStart = start;
    }

    public String getCenter() {
        return mCenter;
    }

    public void setCenter(String center) {
        this.mCenter = center;
    }

    public String getEnd() {
        return mEnd;
    }

    public void setEnd(String end) {
        this.mEnd = end;
    }

    public void setNested(List<LineItemDisplay> items) {
        mNested = items;
    }

    public List<LineItemDisplay> getNested() {
        return mNested;
    }

    public int getNestedColor() {
        return mNestedColor;
    }
}
