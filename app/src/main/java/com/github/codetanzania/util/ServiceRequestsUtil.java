package com.github.codetanzania.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.github.codetanzania.core.api.ApiModelConverter;
import com.github.codetanzania.core.api.RequestsResponse;
import com.github.codetanzania.core.api.model.ApiServiceRequest;
import com.github.codetanzania.core.model.Attachment;
import com.github.codetanzania.core.model.ServiceRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ServiceRequestsUtil {

    public static final String TAG = "ServiceRequestsUtil";

    public static void sort(List<ServiceRequest> requests) {
        Collections.sort(requests, NewestFirstComparator);
    }

    public static RequestsResponse fromResponseBody(Context context, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            ResponseBody data = response.body();
            if (data != null) {
                try {
                    return fromJson(context, data.string());
                } catch (IOException e) {
                    Log.e(TAG, String.format("An error was %s", e.getMessage()));
                }
            }
        }
        return null;
    }

    public static RequestsResponse fromJson(Context context, String json) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat(DateFormat.FULL, DateFormat.FULL)
                .registerTypeAdapter(Date.class, new JsonDateSerializer())
                .create();
        JsonElement jsElement = new JsonParser().parse(json);
        if (jsElement != null) {
            JsonObject jsObject = jsElement.getAsJsonObject();
            JsonElement pages = jsObject.get("pages");
            JsonArray jsArray = jsObject.getAsJsonArray("servicerequests");
            ApiServiceRequest[] requests = gson.fromJson(jsArray, ApiServiceRequest[].class);
            ArrayList<ServiceRequest> requestsWithUri = ApiModelConverter.convert(requests);
            ServiceRequestsUtil.cacheAttachments(context, requestsWithUri);
            return new RequestsResponse(requestsWithUri, pages.getAsInt());
        }
        return null;
    }

    // TODO Move this into API
    // This algorithm translates as :-
    //
    // foreach attachment.content do:
    //    bitmap <- decodeBase64StrToBitmap(attachment.content)
    //    uri    <- compressAndCache(bitmap)
    //    attachment.content := uri
    //  end foreach
    public static boolean cacheAttachments(Context ctx, List<ServiceRequest> requests) {

        ArrayMap<String, Bitmap> bitmapArrayMap = new ArrayMap<>();
        ArrayMap<String, String> uris;
        File albumDir = ImageUtils.getTemporaryAlbumStorageDir(ctx, ImageUtils.TMP_PHOTO_DIR);

        // if we cannot write to the external albums.
        // TODO: replace attachments with the default photo before returning
        if (albumDir == null) {
            return false;
        }

        /*
         * Given Map<ID, Bitmap>. Our goal is to save Bitmaps into the disk and
         * map their corresponding URIs. i.e Map<ID, Bitmap> => Map<ID, URI>.
         *
         * We're using Map<ID, Bitmap> container in order to perform bulk operation (also atomic).
         * Contrary, caching images through for loop iterations would require multiple I/O operations
         */
        for (ServiceRequest request : requests) {
            /*
             * retrieve attachment and persist to the external storage.
             */
            Attachment attachment = request.getAttachment(0);
            if (attachment != null) {
                String key = String.format("%s%s%s",
                        request.id, ImageUtils.IMAGE_TYPE_TOKEN_SEPARATOR, attachment.getMime());

                Log.d(TAG, "The key is " + key);
                if (attachment.getContent() != null) {
                    bitmapArrayMap.put(key, ImageUtils.decodeFromBase64String(attachment.getContent()));
                }
            }
        }

        // cache images. and return their corresponding URIs
        try {
            uris = ImageUtils.compressAndCacheBitmaps(
                    albumDir, bitmapArrayMap, ImageUtils.MAX_COMPRESSION_QUALITY);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


        Log.d(TAG, String.format("%s", uris));

        // replace attachment.content with a uri to the content.
        if (uris != null) {
            for (int i = 0; i < bitmapArrayMap.size(); i++) {
                for (ServiceRequest request : requests) {
                    if (bitmapArrayMap.keyAt(i).startsWith(request.id)) {
                        request.setImageUri(uris.get(request.id));
                        Log.d(TAG, String.format("%s", request.getImageUri()));
                    }
                }
            }
        }

        return true;
    }

    public static Comparator<ServiceRequest> NewestFirstComparator
            = new Comparator<ServiceRequest>() {

        public int compare(ServiceRequest request1, ServiceRequest request2) {
            if (request1 == null || request2 == null) {
                return -1;
            }
            Date firstDate = request1.createdAt;
            Date secondDate = request2.createdAt;

            return secondDate.compareTo(firstDate);
        }

    };
}
