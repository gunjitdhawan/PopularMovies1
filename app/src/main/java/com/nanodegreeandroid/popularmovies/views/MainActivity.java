package com.nanodegreeandroid.popularmovies.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.nanodegreeandroid.popularmovies.AppConstants;
import com.nanodegreeandroid.popularmovies.R;
import com.nanodegreeandroid.popularmovies.models.Movie;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public int currentFlag = AppConstants.ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getMovies(AppConstants.POPULAR);
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
            currentFlag = AppConstants.ALL;
            getMovies(AppConstants.POPULAR);
        }
        if(id == R.id.action_sort_rating)
        {
            currentFlag = AppConstants.ALL;
            getMovies(AppConstants.TOP_RATED);
        }
        if(id == R.id.action_show_fav)
        {
            if(currentFlag == AppConstants.ALL) {
                getMovies(AppConstants.FAV_TAG);
            }
            else
            {
                getMovies(AppConstants.POPULAR);
                currentFlag = AppConstants.ALL;
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
        }

    }

    public void onMovieClicked(Movie movie) {
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
}
