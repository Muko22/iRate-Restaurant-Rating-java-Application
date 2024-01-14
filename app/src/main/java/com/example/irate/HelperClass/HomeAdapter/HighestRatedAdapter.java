package com.example.irate.HelperClass.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.irate.R;

import java.util.ArrayList;

public class HighestRatedAdapter extends RecyclerView.Adapter<HighestRatedAdapter.HighestRatedViewHolder> {

    private RecyclerViewClickListener listener;

    ArrayList<RestaurantCardHelper> highestRatedRestaurants;

    public HighestRatedAdapter(ArrayList<RestaurantCardHelper> highestRatedRestaurants, RecyclerViewClickListener listener) {
        this.highestRatedRestaurants = highestRatedRestaurants;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HighestRatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.highest_rated_card_design, parent, false);
        HighestRatedViewHolder highestRatedViewHolder = new HighestRatedViewHolder(view);
        return highestRatedViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HighestRatedViewHolder holder, int position) {

        RestaurantCardHelper restaurantCardHelper = highestRatedRestaurants.get(position);

        holder.name.setText(restaurantCardHelper.getName());
        holder.category.setText(restaurantCardHelper.getCategory());
        Glide.with(holder.image.getContext()).load(restaurantCardHelper.getUrl()).into(holder.image);
        holder.rating.setRating(restaurantCardHelper.getRating());

    }

    @Override
    public int getItemCount() {
        return highestRatedRestaurants.size();
    }

    public class HighestRatedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView name, category;
        RatingBar rating;

        public HighestRatedViewHolder(@NonNull View itemView) {

            super(itemView);

            // Hooks
            image = itemView.findViewById(R.id.highest_rated_image);
            name = itemView.findViewById(R.id.highest_rated_name);
            category = itemView.findViewById(R.id.highest_rated_category);
            rating = itemView.findViewById(R.id.highest_rated_rating_bar);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

}
