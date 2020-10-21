package com.example.imgurimageapplication.service;


import com.example.imgurimageapplication.model.SearchResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("1")
    Observable<SearchResponse> getSearchImages(@Query("q") String search,
                                               @Header("Authorization") String key);
}
