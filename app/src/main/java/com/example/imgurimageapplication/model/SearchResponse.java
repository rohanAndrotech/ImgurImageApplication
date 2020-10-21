package com.example.imgurimageapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * Search data of response
 */
public class SearchResponse {

    @SerializedName("data")
    @Expose
    private List<DataModel> data = null;

    public List<DataModel> getData() {
        return data;
    }
}
