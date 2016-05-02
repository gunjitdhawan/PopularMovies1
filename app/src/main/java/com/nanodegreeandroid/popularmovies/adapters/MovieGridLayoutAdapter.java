package com.nanodegreeandroid.popularmovies.adapters;

/**
 * Created by gunjit on 02/05/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.nanodegreeandroid.popularmovies.AppConstants;
import com.nanodegreeandroid.popularmovies.models.Movie;
import com.nanodegreeandroid.popularmovies.views.MovieDetailActivity;
import com.nanodegreeandroid.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieGridLayoutAdapter extends CustomAdapter{
    private Activity activity;
    private ArrayList<Movie> movies;

    public MovieGridLayoutAdapter(Activity activity, ArrayList<Movie> movieArrayList) {
        this.activity = activity;
        this.movies = movieArrayList;
    }

    @Override
    public CustomAdapter.CustomRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.movie_grid_item, parent, false);
        ThumbViewHolder dataObjectHolder = new ThumbViewHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final CustomAdapter.CustomRecycleViewHolder holder, final int position) {
        final ThumbViewHolder myHolder = (ThumbViewHolder) holder;

        Log.e("-----", new Gson().toJson(movies.get(position)).toString());

        myHolder.imageThumbView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailsActivity(movies.get(position));
            }
        });
        Picasso.with(activity)
                .load(AppConstants.BASE_IMG_URL+movies.get(position).imagePath)
                .placeholder(R.drawable.ic_placeholder)
                .into((myHolder.imageThumbView));
    }

    private void startDetailsActivity(Movie movie) {
        Intent i = new Intent(activity, MovieDetailActivity.class);
        i.putExtra("movie", movie);
        activity.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ThumbViewHolder extends CustomAdapter.CustomRecycleViewHolder {
        private ImageView imageThumbView;

        public ThumbViewHolder(View itemView) {
            super(itemView);
            imageThumbView = (ImageView) itemView.findViewById(R.id.movie_thumb);
        }
    }
}