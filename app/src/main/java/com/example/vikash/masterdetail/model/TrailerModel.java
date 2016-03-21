package com.example.vikash.masterdetail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class TrailerModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TrailerData> results = new ArrayList<>();

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The results
     */
    public List<TrailerData> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<TrailerData> results) {
        this.results = results;
    }

}