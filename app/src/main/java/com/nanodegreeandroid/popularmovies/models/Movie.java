package com.nanodegreeandroid.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gunjit on 02/05/16.
 */
public class Movie implements Parcelable{
    @SerializedName("poster_path")
    public String imagePath;

    @SerializedName("original_title")
    public String title;

    @SerializedName("release_date")
    public String releaseDate;

    @SerializedName("vote_average")
    public double voteAverage;

    @SerializedName("overview")
    public String plotSynopsis;

    @SerializedName("popularity")

    public double popularity;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imagePath);
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.plotSynopsis);
        dest.writeDouble(this.popularity);
    }

    protected Movie(Parcel in) {
        this.imagePath = in.readString();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.voteAverage = in.readDouble();
        this.plotSynopsis = in.readString();
        this.popularity = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
