package com.github.codetanzania.util;


import android.content.Context;
import android.support.annotation.NonNull;

import com.github.codetanzania.core.gson.ListWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class StaticAssetsUtils {

    private static final Gson sGson = new GsonBuilder().setLenient().create();

    public static <T> List<T> loadJsonAsItems(@NonNull Context ctx,
        @NonNull String filename, @NonNull Class<T> tClass) {

        BufferedReader br = null;
        // TODO: convert to an elegant solution: try-with-resources (requires KITKAT/API 19+)
        // try (BufferedReader br = new BufferedReader(new InputStreamReader(ctx.getAssets().open(filename)))) {
        //    String json = "";
        //    for (String line; !Util.isNull(line = br.readLine())) {
        //       json += line;
        //    }
        //    return parseJSON(json);
        // }
        try {

            br = new BufferedReader(new InputStreamReader(
                    ctx.getAssets().open(filename)));
            String json = "";

            for (String line; !Util.isNull(line = br.readLine());) {
                json += line;
            }

            return parseJson(json, tClass);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ioException) {
                // ignore
            }
        }
    }

    private static <T> List<T> parseJson(@NonNull String json, @NonNull Class<T> tClass) {
        return sGson.fromJson(json, new ListWrapper<>(tClass));
    }
}
