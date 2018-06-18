package com.github.codetanzania.core.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.codetanzania.core.model.ServiceGroup;
import com.github.codetanzania.core.model.adapter.ServiceRequests;
import com.github.codetanzania.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tz.co.codetanzania.BuildConfig;

/**
 * As of June 6, 2018, this is being used only for posting new issues from the "Report Issue" page.
 * All other network calls are made with the MajiFixAPI in the library-core.
 *
 * This was deprecated with the build out and integration of the library-core. In the future, please
 * use MajiFixAPI for all network calls.
 */
@Deprecated
public class Open311Api {

    public interface ServiceGroupEndpoint {
        @GET("/servicegroups")
        @Headers({
                "Content-Type: application/json; charset='utf-8'",
                "Accept: application/json"
        })
        Call<List<ServiceGroup>> get(@Header("Authorization") String jwtToken);
    }

    public interface SendSMSEndpoint {
        @POST("/messages")
        @Headers({
                "Content-Type: application/json"
        })
        Call<ResponseBody> sendSms(
           @Header("Authorization") String authorization,
           @Body Map<String, String> body);
    }

    public interface ServiceRequestEndpoint {
        @POST("/servicerequests")
        @Headers({"Content-Type: application/json"})
        Call<ResponseBody> openTicket(@Header("Authorization") String authorization, @Body Map<String, Object> body);

        @GET("/servicerequests")
        @Headers({"Accept: application/json"})
        Call<ServiceRequests> getByUserId(/*@Header("Authorization")*/String jwtToken);

        @GET("/servicerequests")
        @Headers({"Accept: application/json"})
        Call<ResponseBody> getByUserId(@Header("Authorization") String authorization, @Query("query")String query);

        @GET("/servicerequests")
        @Headers({"Accept: application/json"})
        Call<ResponseBody> getPageByUserId(@Header("Authorization") String authorization,
                                           @Query("query")String query,
                                           @Query("page") int page);


        @GET("/servicerequests")
        @Headers({"Accept: application/json"})
        Call<ResponseBody> getByTicketId(@Header("Authorization") String authorization, @Query("query")String query);
    }

    public interface AuthEndpoint {
        @POST("/signin")
        @Headers({"Accept: application/json", "Content-Type: application/json"})
        Call<ResponseBody> signIn(@Body Map<String, String> reporter);
    }

    interface ServicesEndpoint {
        @GET("/services")
        @Headers({"Accept: application/json", "Content-Type: application/json"})
        Call<ResponseBody> getAll(@Header("Authorization") String authHeader, @Query("query") String query);
    }

    public static class ServiceBuilder {

        private Context mContext;

        public ServiceBuilder(Context mContext) {
            this.mContext = mContext;
        }

        private Retrofit retrofit() {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            return new Retrofit.Builder()
                    .baseUrl(BuildConfig.END_POINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        public ServiceRequestEndpoint getServiceRequests() {
            return retrofit().create(ServiceRequestEndpoint.class);
        }

        public Call<ResponseBody> getIssuesByUser(@NonNull String phone,
                                                  Callback<ResponseBody> callback){
            String queryParams = getQueryForIssuesByUser(phone);
            return build(Open311Api.ServiceRequestEndpoint.class)
                    .getByUserId(Util.getAuthToken(), queryParams);
        }

        public Call<ResponseBody> getPageOfIssuesByUser(@NonNull String phone, int page,
                                                  Callback<ResponseBody> callback){
            String queryParams = getQueryForIssuesByUser(phone);
            return build(Open311Api.ServiceRequestEndpoint.class)
                    .getPageByUserId(Util.getAuthToken(), queryParams, page);
        }

        public Call<ResponseBody> getAllPublicCategories() {
            // limit the response to return publicly displayed service categories
            Map<String, String> query = new HashMap<>();
            query.put("isExternal", "true");
            GsonBuilder gsonBuilder = new GsonBuilder().setLenient();
            String queryString = gsonBuilder.create().toJson(query);

            return build(Open311Api.ServicesEndpoint.class).getAll(Util.getAuthToken(), queryString);
        }

        public <T> T build(Class<T> clazz) {
            return retrofit().create(clazz);
        }

        private String getQueryForIssuesByUser(@NonNull String phone) {
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("reporter.phone", phone);
            GsonBuilder gsonBuilder = new GsonBuilder().setLenient();
            return gsonBuilder.create().toJson(queryMap);
        }
    }
}
