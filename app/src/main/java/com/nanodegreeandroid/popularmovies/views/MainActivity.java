package com.nanodegreeandroid.popularmovies.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.nanodegreeandroid.popularmovies.AppConstants;
import com.nanodegreeandroid.popularmovies.AppUtils;
import com.nanodegreeandroid.popularmovies.R;
import com.nanodegreeandroid.popularmovies.networking.VolleySingleton;
import com.nanodegreeandroid.popularmovies.adapters.CustomAdapter;
import com.nanodegreeandroid.popularmovies.adapters.MovieGridLayoutAdapter;
import com.nanodegreeandroid.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    RecyclerView movieRecyclerView;
    GridLayoutManager mLayoutManager;
    CustomAdapter movieGridAdapter;
    ProgressDialog movieDialog;
    ArrayList<Movie> movieList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeViews();

        sendRequestForMovies(AppConstants.POPULAR);
        }

    private void sendRequestForMovies(String criteria) {

        if(!AppUtils.isNetworkAvailable(MainActivity.this))
        {
            Toast.makeText(MainActivity.this, "Please connect to internet!", Toast.LENGTH_SHORT).show();
            return;
        }

        movieDialog.show();

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                AppConstants.BASE_URL+criteria+"?api_key="+AppConstants.API_KEY, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SUCCESS", response.toString());
                        movieList.clear();
                        try {
                            JSONArray movieArray = response.getJSONArray("results");
                            for(int i = 0; i <movieArray.length(); i++)
                            {
                                Movie movie = new Gson().fromJson(movieArray.get(i).toString(), Movie.class);
                                movieList.add(movie);
                                movieGridAdapter.notifyDataSetChanged();
                            }
                            movieDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            movieDialog.dismiss();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());
                movieDialog.dismiss();
            }
        });

        queue.add(jsonObjReq);

    }

    private void initializeViews() {

        movieDialog = new ProgressDialog(MainActivity.this);
        movieDialog.setMessage("Please wait...");
        movieDialog.setIndeterminate(true);
        movieDialog.setCancelable(false);
        movieDialog.setCanceledOnTouchOutside(false);

        movieRecyclerView = (RecyclerView) findViewById(R.id.movie_list);
        movieRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        movieRecyclerView.setLayoutManager(mLayoutManager);
        movieGridAdapter = new MovieGridLayoutAdapter(this, movieList);
        movieRecyclerView.setAdapter(movieGridAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_popularity) {
            sendRequestForMovies(AppConstants.POPULAR);
        }
        if(id == R.id.action_sort_rating)
        {
            sendRequestForMovies(AppConstants.TOP_RATED);
        }

        return super.onOptionsItemSelected(item);
    }

}
