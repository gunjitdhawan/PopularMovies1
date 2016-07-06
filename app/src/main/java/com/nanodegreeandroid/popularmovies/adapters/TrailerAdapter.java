package com.nanodegreeandroid.popularmovies.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nanodegreeandroid.popularmovies.AppConstants;
import com.nanodegreeandroid.popularmovies.R;
import com.nanodegreeandroid.popularmovies.models.Trailer;

import java.util.ArrayList;

/**
 * Created by gunjit on 29/06/16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    Context context;
    ArrayList<Trailer> trailers;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailerArrayList) {
        this.context = context;
        this.trailers = trailerArrayList;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.trailer_item, parent, false);
        TrailerAdapterViewHolder dataObjectHolder = new TrailerAdapterViewHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {

        final Trailer trailer = trailers.get(position);
        holder.trailerName.setText(trailer.name);
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYoutube(trailer.source);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView trailerName;
        LinearLayout parentView;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            trailerName = (TextView) itemView.findViewById(R.id.trailer_name);
            parentView = (LinearLayout) itemView.findViewById(R.id.parentView);
        }
    }
    public void openYoutube(String id){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(AppConstants.YOUTUBE_BASE_URL + id));
            context.startActivity(intent);
        }
    }
}
