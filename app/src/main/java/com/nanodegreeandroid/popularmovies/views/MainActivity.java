package com.nanodegreeandroid.popularmovies.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.nanodegreeandroid.popularmovies.AppConstants;
import com.nanodegreeandroid.popularmovies.R;
import com.nanodegreeandroid.popularmovies.models.Movie;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public int currentFlag = AppConstants.ALL_POP;
    public Movie selectedMovie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        try {
            currentFlag = savedInstanceState.getInt("MyInt");
        }
        catch (NullPointerException e)
        {

        }
        Log.e("oncreate", ""+currentFlag);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if(currentFlag == AppConstants.ALL_POP)
        {
            getMovies(AppConstants.POPULAR);
        }
        else if(currentFlag == AppConstants.ALL_RAT)
        {
            getMovies(AppConstants.TOP_RATED);
        }
        else if(currentFlag == AppConstants.ONLY_FAV)
        {
            getMovies(AppConstants.FAV_TAG);
        }

    }

    private void getMovies(String type) {
        MovieListFragment movieListFragment = new MovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        movieListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_1, movieListFragment)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_show_fav);
        if(currentFlag==AppConstants.ONLY_FAV)
        {
            menuItem.setTitle(getResources().getString(R.string.action_show_all));
        }
        else {
            menuItem.setTitle(getResources().getString(R.string.action_show_favorite));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_popularity) {
            currentFlag = AppConstants.ALL_POP;
            getMovies(AppConstants.POPULAR);
        }
        if(id == R.id.action_sort_rating)
        {
            currentFlag = AppConstants.ALL_RAT;
            getMovies(AppConstants.TOP_RATED);
        }
        if(id == R.id.action_show_fav)
        {
            if(currentFlag == AppConstants.ALL_POP || currentFlag == AppConstants.ALL_RAT ) {
                getMovies(AppConstants.FAV_TAG);
            }
            else
            {
                getMovies(AppConstants.POPULAR);
                currentFlag = AppConstants.ALL_POP;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    public void onMovieListLoaded(ArrayList<Movie> movieList) {
        if(findViewById(R.id.fragment_container_2)!=null) {
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("movie", movieList.get(0));
            movieDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, movieDetailFragment)
                    .commit();
            Log.e("onMovieListLoaded", new Gson().toJson(selectedMovie));
            if(selectedMovie!=null) {
                onMovieClicked(selectedMovie);
            }
        }

    }

    public void onMovieClicked(Movie movie) {
        selectedMovie = movie;
        if(findViewById(R.id.fragment_container_2)==null) {
            Intent i = new Intent(MainActivity.this, MovieDetailActivity.class);
            i.putExtra("movie", movie);
            startActivity(i);
        }
        else
        {
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("movie", movie);
            movieDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, movieDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt("MyInt", currentFlag);
        savedInstanceState.putParcelable("movie", selectedMovie);
        Log.e("onSaveInstanceState", ""+currentFlag);
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        currentFlag = savedInstanceState.getInt("MyInt");
        selectedMovie = savedInstanceState.getParcelable("movie");
        Log.e("onRestoreInstanceState", ""+currentFlag);


    }
}
