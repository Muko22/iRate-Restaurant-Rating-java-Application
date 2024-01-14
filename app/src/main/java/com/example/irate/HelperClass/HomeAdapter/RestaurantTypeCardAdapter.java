package com.example.irate.HelperClass.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.irate.R;

import java.util.ArrayList;

public class RestaurantTypeCardAdapter extends RecyclerView.Adapter<RestaurantTypeCardAdapter.CategoryCardViewHolder> {

    private RestaurantTypeCardAdapter.RecyclerViewClickListener listener;

    ArrayList<RestaurantTypeCardHelper> categories;

    public RestaurantTypeCardAdapter(ArrayList<RestaurantTypeCardHelper> categories, RecyclerViewClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RestaurantTypeCardAdapter.CategoryCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_design, parent, false);
        RestaurantTypeCardAdapter.CategoryCardViewHolder categoryCardViewHolder = new RestaurantTypeCardAdapter.CategoryCardViewHolder(view);
        return categoryCardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantTypeCardAdapter.CategoryCardViewHolder holder, int position) {

        RestaurantTypeCardHelper restaurantTypeCardHelper = categories.get(position);

        holder.relativeLayout.setBackground(restaurantTypeCardHelper.getGradient());
        holder.category.setText(restaurantTypeCardHelper.getCategory());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView category;
        RelativeLayout relativeLayout;

        public CategoryCardViewHolder(@NonNull View itemView) {

            super(itemView);

            // Hooks
            relativeLayout = itemView.findViewById(R.id.background_gradient);
            category = itemView.findViewById(R.id.category);
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
