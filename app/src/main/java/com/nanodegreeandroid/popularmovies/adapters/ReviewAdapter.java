package com.nanodegreeandroid.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegreeandroid.popularmovies.R;
import com.nanodegreeandroid.popularmovies.models.Review;

import java.util.ArrayList;

/**
 * Created by gunjit on 29/06/16.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    Context context;
    ArrayList<Review> reviews;

    public ReviewAdapter(Context context, ArrayList<Review> trailerArrayList) {
        this.context = context;
        this.reviews = trailerArrayList;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.review_item, parent, false);
        ReviewAdapterViewHolder dataObjectHolder = new ReviewAdapterViewHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {

        final Review review = reviews.get(position);
        holder.reviewAuthor.setText("- "+review.author);
        holder.reviewContent.setText(review.content);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView reviewContent;
        TextView reviewAuthor;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            reviewContent = (TextView) itemView.findViewById(R.id.review_content);
            reviewAuthor = (TextView) itemView.findViewById(R.id.review_author);
        }
    }
    
}
