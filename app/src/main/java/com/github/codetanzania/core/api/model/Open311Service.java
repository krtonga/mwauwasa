package com.github.codetanzania.core.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.github.codetanzania.core.model.Priority;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// @Table(name = "open311Service", _id = BaseColumns._ID)
public class Open311Service implements Parcelable, Comparable<Open311Service> {

    private static final String TAG = "Open311Service";

    private transient static final String ID   = "_id";
    private transient static final String CODE = "code";
    private transient static final String NAME = "name";
    private transient static final String DESCRIPTION = "description";
    private transient static final String COLOR = "color";
    private transient static final String PRIORITY = "priority";

    public String id;

    // @Column(name = "code")
    public String code;

    // @Column(name = "name")
    public String name;

    // @Column(name = "description")
    public String description;

    // @Column(name = "color")
    public String color;

    public Priority priority;

    public Open311Service() {}

    protected Open311Service(Parcel in) {
        id   = in.readString();
        code = in.readString();
        name = in.readString();
        description = in.readString();
        color = in.readString();
        priority = in.readParcelable(Priority.class.getClassLoader());
    }

    public static List<Open311Service> fromJson(String jsonStr) throws JSONException {
        List<Open311Service> res = new ArrayList<>();

        JSONArray jsonArray = null;

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            jsonArray   = jsonObject.getJSONArray("services");
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Open311Service open311Service = new Open311Service();
                open311Service.id   = obj.getString(ID);
                open311Service.name = obj.getString(NAME);
                open311Service.code = obj.getString(CODE);
                open311Service.description = obj.getString(DESCRIPTION);
                open311Service.color = obj.getString(COLOR);
                try {
                    JSONObject priorityObj = obj.getJSONObject(PRIORITY);
                    open311Service.priority = Priority.fromJsonObject(priorityObj);
                } catch(JSONException exception) {
                    open311Service.priority = Priority.getDefaultPriority();
                }
                res.add(open311Service);
            }
        }

        return res;
    }

    public static final Creator<Open311Service> CREATOR = new Creator<Open311Service>() {
        @Override
        public Open311Service createFromParcel(Parcel in) {
            return new Open311Service(in);
        }

        @Override
        public Open311Service[] newArray(int size) {
            return new Open311Service[size];
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
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(color);
        parcel.writeParcelable(priority, i);
    }

    @Override
    public String toString() {
        return "Open311Service{" +
                "id='" + id + '\'' +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", priority=" + priority +
                '}';
    }

    @Override
    public int compareTo(@NonNull Open311Service o) {
        return priority.compareTo(o.priority);
    }
}
