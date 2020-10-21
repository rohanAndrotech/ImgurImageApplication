package com.example.imgurimageapplication.service;

import com.example.imgurimageapplication.util.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ApiClient class.
 */
public class ApiClient {
    private static Retrofit retrofit = null;
    static OkHttpClient.Builder client = new OkHttpClient.Builder();

    /**
     * Retrofit Builder implementation.
     *
     * @return retrofit object.
     */
    public static Retrofit getApiClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}