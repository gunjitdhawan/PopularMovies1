package com.nanodegreeandroid.popularmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gunjit on 29/06/16.
 */
public class Trailer {

    @SerializedName("name")
    public String name;

    @SerializedName("size")
    public String size;

    @SerializedName("source")
    public String source;

    @SerializedName("type")
    public String type;
}
