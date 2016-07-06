package com.nanodegreeandroid.popularmovies.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.nanodegreeandroid.popularmovies.adapters.MovieGridLayoutAdapter;
import com.nanodegreeandroid.popularmovies.models.Movie;
import com.nanodegreeandroid.popularmovies.networking.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gunjit on 29/06/16.
 */
public class MovieListFragment extends Fragment {

    RecyclerView movieRecyclerView;
    GridLayoutManager mLayoutManager;
    MovieGridLayoutAdapter movieGridAdapter;
    ProgressDialog movieDialog;
    ArrayList<Movie> movieList = new ArrayList<>();
    MainActivity mainActivity;


    public MovieListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        initializeViews(view);

        mainActivity = ((MainActivity) getActivity());
        String type  = this.getArguments().getString("type");
        sendRequestForMovies(type);

        return view;
    }

    private void sendRequestForMovies(String criteria) {

        if(!AppUtils.isNetworkAvailable(getActivity()))
        {
            Toast.makeText(getActivity(), "Please connect to internet!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(criteria.equalsIgnoreCase(AppConstants.FAV_TAG))
        {
            ArrayList<Movie> movieList = movieGridAdapter.getCurrentFavMovies();
            if(movieList!=null && movieList.size()>0)
            {
                this.movieList.clear();
                this.movieList.addAll(movieList);
                movieGridAdapter.notifyDataSetChanged();
                mainActivity.currentFlag = AppConstants.ONLY_FAV;
            }
            else
            {
                Toast.makeText(getActivity(), "You don't have any favourite movies!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        movieDialog.show();

        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        Log.d("REQUEST", AppConstants.BASE_URL+criteria+"?api_key="+AppConstants.API_KEY);
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                AppConstants.BASE_URL+criteria+"?api_key="+AppConstants.API_KEY, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SUCCESS", response.toString());
                        movieList.clear();
                        movieGridAdapter.notifyDataSetChanged();
                        try {
                            JSONArray movieArray = response.getJSONArray("results");
                            for(int i = 0; i <movieArray.length(); i++)
                            {
                                Movie movie = new Gson().fromJson(movieArray.get(i).toString(), Movie.class);
                                movieList.add(movie);
                                movieGridAdapter.notifyDataSetChanged();
                            }
                            mainActivity.onMovieListLoaded(movieList);
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

    private void initializeViews(View view) {

        movieDialog = new ProgressDialog(getActivity());
        movieDialog.setMessage("Please wait...");
        movieDialog.setIndeterminate(true);
        movieDialog.setCancelable(false);
        movieDialog.setCanceledOnTouchOutside(false);

        movieRecyclerView = (RecyclerView) view.findViewById(R.id.movie_list);
        movieRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        movieRecyclerView.setLayoutManager(mLayoutManager);
        movieGridAdapter = new MovieGridLayoutAdapter(getActivity(), movieList, mainActivity);
        movieRecyclerView.setAdapter(movieGridAdapter);
    }
}
