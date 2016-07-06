package com.nanodegreeandroid.popularmovies.adapters;

/**
 * Created by gunjit on 02/05/16.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nanodegreeandroid.popularmovies.AppConstants;
import com.nanodegreeandroid.popularmovies.R;
import com.nanodegreeandroid.popularmovies.models.Movie;
import com.nanodegreeandroid.popularmovies.views.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieGridLayoutAdapter extends CustomAdapter{
    private Activity activity;
    private ArrayList<Movie> movies;
    private MainActivity mainActivity;

    public MovieGridLayoutAdapter(Activity activity, ArrayList<Movie> movieArrayList, MainActivity mainActivity) {
        this.activity = activity;
        this.movies = movieArrayList;
        this.mainActivity = mainActivity;
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

        final Movie movie = movies.get(position);
        ArrayList<Movie> currentFavMovies = getCurrentFavMovies();

        if(currentFavMovies!=null && currentFavMovies.size()>0)
        {
            if(currentFavMovies.contains(movie))
            {
                movie.isFav = true;
                myHolder.favBtn.setImageResource(R.drawable.lit_star);
                Log.e("AFav", movie.title);
            }
            else
            {
                movie.isFav = false;
                myHolder.favBtn.setImageResource(R.drawable.unlit_star);
                Log.e("NFav", movie.title);
            }
        }


        myHolder.imageThumbView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailsActivity(movie);
            }
        });
        Picasso.with(activity)
                .load(AppConstants.BASE_IMG_URL+movies.get(position).imagePath)
                .placeholder(R.drawable.ic_placeholder)
                .into((myHolder.imageThumbView));

        myHolder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("FavBtn", "clicked "+movie.title);

                if(!movie.isFav)
                {
                    myHolder.favBtn.setImageResource(R.drawable.lit_star);
                    addToFavList(movie);
                    movie.isFav = true;
                }
                else
                {
                    myHolder.favBtn.setImageResource(R.drawable.unlit_star);
                    removeFromFavList(movie);
                    movie.isFav = false;
                }
            }
        });
    }

    private void removeFromFavList(Movie movie) {
        Log.e("favMovies", "Removing "+movie.title);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        ArrayList<Movie> movieList = getCurrentFavMovies();
        movieList.remove(movie);
        JsonElement element =
                gson.toJsonTree(movieList , new TypeToken<List<Movie>>() {}.getType());
        editor.putString(AppConstants.FAV_TAG, element.toString());
        editor.commit();
    }

    private void addToFavList(Movie movie) {
        Log.e("favMovies", "Adding "+movie.title);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        ArrayList<Movie> movieList = getCurrentFavMovies();
        movieList.add(movie);
        JsonArray element =
                gson.toJsonTree(movieList , new TypeToken<List<Movie>>() {}.getType()).getAsJsonArray();
        editor.putString(AppConstants.FAV_TAG, element.toString());
        editor.commit();
    }

    public ArrayList<Movie> getCurrentFavMovies() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String favMovies = sharedPrefs.getString(AppConstants.FAV_TAG, null);
        Log.e("favMovies", ""+favMovies);
        if(favMovies!=null) {
            JsonParser jsonParser = new JsonParser();
            JsonArray moviesArray = (JsonArray) jsonParser.parse(favMovies);

            if(moviesArray!=null && moviesArray.size()>0) {
                return new Gson().fromJson(moviesArray, new TypeToken<List<Movie>>() {
                }.getType());
            }
            else{
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    private void startDetailsActivity(Movie movie) {
        ((MainActivity)activity).onMovieClicked(movie);
//        Intent i = new Intent(activity, MovieDetailActivity.class);
//        i.putExtra("movie", movie);
//        activity.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ThumbViewHolder extends CustomAdapter.CustomRecycleViewHolder {
        private ImageView imageThumbView;
        private ImageView favBtn;

        public ThumbViewHolder(View itemView) {
            super(itemView);
            imageThumbView = (ImageView) itemView.findViewById(R.id.movie_thumb);
            favBtn = (ImageView) itemView.findViewById(R.id.fav_btn);
        }
    }
}