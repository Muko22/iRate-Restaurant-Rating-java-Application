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

public class RecentlyAddedAdapter extends RecyclerView.Adapter<RecentlyAddedAdapter.RecentlyAddedViewHolder> {

    private RecentlyAddedAdapter.RecyclerViewClickListener listener;

    ArrayList<RestaurantCardHelper> recentlyAddedRestaurants;

    public RecentlyAddedAdapter (ArrayList<RestaurantCardHelper> recentlyAddedRestaurants, RecentlyAddedAdapter.RecyclerViewClickListener listener) {
        this.recentlyAddedRestaurants = recentlyAddedRestaurants;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecentlyAddedAdapter.RecentlyAddedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recently_added_card_design, parent, false);
        RecentlyAddedAdapter.RecentlyAddedViewHolder recentlyAddedViewHolder = new RecentlyAddedAdapter.RecentlyAddedViewHolder(view);
        return recentlyAddedViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyAddedAdapter.RecentlyAddedViewHolder holder, int position) {

        RestaurantCardHelper restaurantCardHelper = recentlyAddedRestaurants.get(position);

        holder.name.setText(restaurantCardHelper.getName());
        holder.category.setText(restaurantCardHelper.getCategory());
        Glide.with(holder.image.getContext()).load(restaurantCardHelper.getUrl()).into(holder.image);
        holder.rating.setRating(restaurantCardHelper.getRating());

    }

    @Override
    public int getItemCount() {
        return recentlyAddedRestaurants.size();
    }

    public class RecentlyAddedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView name, category;
        RatingBar rating;

        public RecentlyAddedViewHolder(@NonNull View itemView) {

            super(itemView);

            // Hooks
            image = itemView.findViewById(R.id.recently_added_image);
            name = itemView.findViewById(R.id.recently_added_name);
            category = itemView.findViewById(R.id.recently_added_category);
            rating = itemView.findViewById(R.id.recently_added_rating_bar);
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
