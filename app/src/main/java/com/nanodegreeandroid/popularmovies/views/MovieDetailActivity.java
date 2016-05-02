package com.nanodegreeandroid.popularmovies.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanodegreeandroid.popularmovies.AppConstants;
import com.nanodegreeandroid.popularmovies.R;
import com.nanodegreeandroid.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    Movie movie;

//    @BindView(R.id.detail_title)TextView title;
//    @BindView(R.id.details_release_date)TextView releaseDate;
//    @BindView(R.id.details_vote_average)TextView averageVote;
//    @BindView(R.id.detail_synopsis)TextView synopsis;
//    @BindView(R.id.detail_poster)ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        ButterKnife.setDebug(true);
//        ButterKnife.bind(MovieDetailActivity.this);

        movie = (Movie) getIntent().getExtras().get("movie");



        ((TextView)findViewById(R.id.detail_title)).setText(movie.title);
        ((TextView)findViewById(R.id.details_release_date)).setText(movie.releaseDate);
        ((TextView)findViewById(R.id.details_vote_average)).setText(movie.voteAverage+"/10");
        ((TextView)findViewById(R.id.detail_synopsis)).setText(movie.plotSynopsis);
        Picasso.with(MovieDetailActivity.this).load(AppConstants.BASE_IMG_URL+movie.imagePath).into((ImageView) findViewById(R.id.detail_poster));
    }

}
