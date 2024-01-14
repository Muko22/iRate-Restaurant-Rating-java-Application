package com.example.irate.HelperClass.SearchAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.irate.HelperClass.HomeAdapter.RestaurantCardHelper;
import com.example.irate.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{

    private RecyclerViewClickListener listener;

    ArrayList<RestaurantCardHelper> restaurants;

    public SearchAdapter(ArrayList<RestaurantCardHelper> restaurants, RecyclerViewClickListener listener) {
        this.restaurants = restaurants;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card_design, parent, false);
        SearchViewHolder searchViewHolder = new SearchViewHolder(view);
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        RestaurantCardHelper restaurantCardHelper = restaurants.get(position);

        holder.name.setText(restaurantCardHelper.getName());
        holder.category.setText(restaurantCardHelper.getCategory());
        Glide.with(holder.image.getContext()).load(restaurantCardHelper.getUrl()).into(holder.image);
        holder.rating.setRating(restaurantCardHelper.getRating());

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView name, category;
        RatingBar rating;

        public SearchViewHolder(@NonNull View itemView) {

            super(itemView);

            // Hooks
            image = itemView.findViewById(R.id.search_card_image);
            name = itemView.findViewById(R.id.search_card_name);
            category = itemView.findViewById(R.id.search_card_category);
            rating = itemView.findViewById(R.id.search_card_rating_bar);
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

    public void filterRestaurants(ArrayList<RestaurantCardHelper> filteredRestaurants) {
        restaurants = filteredRestaurants;
        notifyDataSetChanged();
    }

}
