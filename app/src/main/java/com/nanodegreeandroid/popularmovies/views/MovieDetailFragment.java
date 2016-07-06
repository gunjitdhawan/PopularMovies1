package com.nanodegreeandroid.popularmovies.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.nanodegreeandroid.popularmovies.AppConstants;
import com.nanodegreeandroid.popularmovies.R;
import com.nanodegreeandroid.popularmovies.adapters.ReviewAdapter;
import com.nanodegreeandroid.popularmovies.adapters.TrailerAdapter;
import com.nanodegreeandroid.popularmovies.models.Movie;
import com.nanodegreeandroid.popularmovies.models.Review;
import com.nanodegreeandroid.popularmovies.models.Trailer;
import com.nanodegreeandroid.popularmovies.networking.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gunjit on 29/06/16.
 */
public class MovieDetailFragment extends Fragment {

    Movie movie;
    ArrayList<Review> reviewList = new ArrayList<>();
    ArrayList<Trailer> trailerList = new ArrayList<>();

    ReviewAdapter reviewAdapter;
    TrailerAdapter trailerAdapter;

    RecyclerView reviewRecyclerView;
    RecyclerView trailerRecyclerView;

    ProgressDialog progressDialog;

    public MovieDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_movie_detail, container, false);
        Bundle bundle = this.getArguments();
        movie = (Movie) bundle.getParcelable("movie");

        Log.e("-----", "3");
        Log.e("movie", new Gson().toJson(movie));
        ((TextView)view.findViewById(R.id.detail_title)).setText(movie.title);
        ((TextView)view.findViewById(R.id.details_release_date)).setText(movie.releaseDate);
        ((TextView)view.findViewById(R.id.details_vote_average)).setText(movie.voteAverage+"/10");
        ((TextView)view.findViewById(R.id.detail_synopsis)).setText(movie.plotSynopsis);
        Picasso.with(getActivity()).load(AppConstants.BASE_IMG_URL+movie.imagePath).into((ImageView) view.findViewById(R.id.detail_poster));

        reviewAdapter = new ReviewAdapter(getActivity(), reviewList);
        trailerAdapter = new TrailerAdapter(getActivity(), trailerList);

        reviewRecyclerView = (RecyclerView) view.findViewById(R.id.review_list);
        trailerRecyclerView = (RecyclerView) view.findViewById(R.id.trailer_list);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reviewRecyclerView.setAdapter(reviewAdapter);

        trailerRecyclerView.setHasFixedSize(true);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        trailerRecyclerView.setAdapter(trailerAdapter);


        setupReviews(movie.id);
        setupTrailers(movie.id);
        return view;
    }

    private void setupReviews(String id)
    {
        progressDialog.show();
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        Log.d("REQUEST", AppConstants.BASE_URL+id+"/"+AppConstants.REVIEWS+"?api_key="+AppConstants.API_KEY);
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                AppConstants.BASE_URL+id+"/"+AppConstants.REVIEWS+"?api_key="+AppConstants.API_KEY, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SUCCESS : Reviews", response.toString());
                        reviewList.clear();
                        try {
                            JSONArray reviewArray = response.getJSONArray("results");
                            for(int i = 0; i <reviewArray.length(); i++)
                            {
                                Review review = new Gson().fromJson(reviewArray.get(i).toString(), Review.class);
                                reviewList.add(review);
                                reviewAdapter.notifyDataSetChanged();
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }
        });

        queue.add(jsonObjReq);
    }

    private void setupTrailers(String id)
    {
        progressDialog.show();
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        Log.d("REQUEST", AppConstants.BASE_URL+id+"/"+AppConstants.TRAILER+"?api_key="+AppConstants.API_KEY);
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                AppConstants.BASE_URL+id+"/"+AppConstants.TRAILER+"?api_key="+AppConstants.API_KEY, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SUCCESS: Trailers", response.toString());
                        trailerList.clear();
                        try {
                            JSONArray trailerArray = response.getJSONArray("youtube");
                            for(int i = 0; i <trailerArray.length(); i++)
                            {
                                Trailer trailer = new Gson().fromJson(trailerArray.get(i).toString(), Trailer.class);
                                trailerList.add(trailer);
                                trailerAdapter.notifyDataSetChanged();
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }
        });

        queue.add(jsonObjReq);
    }
}
