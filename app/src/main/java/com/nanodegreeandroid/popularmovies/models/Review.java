package com.nanodegreeandroid.popularmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gunjit on 29/06/16.
 */
public class Review {

    @SerializedName("id")
    public String id;

    @SerializedName("author")
    public String author;

    @SerializedName("content")
    public String content;
}
