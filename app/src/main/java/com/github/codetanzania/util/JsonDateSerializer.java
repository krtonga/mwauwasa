package com.github.codetanzania.util;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kmoze on 6/14/17.
 */
public class JsonDateSerializer implements JsonSerializer<Date>, JsonDeserializer {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return null == src ? null : new JsonPrimitive(sdf.format(src));
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (null == json) {
            return null;
        } else {
            try {
                Log.d("ServiceRequestsUtil", json.getAsString());
                return sdf.parse(json.getAsString());
            } catch (Exception e) {
                Log.e("ServiceRequestsUtil", e.getMessage());
                return null;
            }
        }
    }
}
