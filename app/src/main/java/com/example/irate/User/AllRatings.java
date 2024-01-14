package com.example.irate.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.irate.HelperClass.HomeAdapter.RestaurantCardHelper;
import com.example.irate.HelperClass.SearchAdapter.SearchAdapter;
import com.example.irate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllRatings extends AppCompatActivity {

    // Variables
    RecyclerView allRatingsRecycler;
    RecyclerView.Adapter adapter;
    private SearchAdapter.RecyclerViewClickListener searchListener;
    ArrayList<RestaurantCardHelper> restaurants = new ArrayList<>();
    TextView noRatings;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_ratings);

        // Hooks
        allRatingsRecycler = findViewById(R.id.all_ratings_recycler);
        noRatings = findViewById(R.id.all_ratings_no_ratings);
        backButton = findViewById(R.id.all_ratings_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Call Recycler Function
        myRatingsRecycler();

    }

    private void myRatingsRecycler() {

        allRatingsOnClickListener();

        allRatingsRecycler.setHasFixedSize(true);
        allRatingsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ratings");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String imageUrl = "https://i.picsum.photos/id/851/600/600.jpg?hmac=6cm2uCNhm0ILLLb5cX3VyyfS2XiYk6N_uuLo23wRAa0";

                if (snapshot.exists()) {
                    noRatings.setVisibility(View.INVISIBLE);
                    restaurants.clear();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        String id = ds.getKey();
                        String restaurantName = ds.child("restaurantName").getValue().toString();
                        String restaurantType = ds.child("restaurantType").getValue().toString();
                        if (ds.child("URL").exists()) {
                            imageUrl = ds.child("URL").getValue().toString();
                        }
                        float rating = (Float.parseFloat(ds.child("averageRating").getValue().toString()))*-1;
                        restaurants.add(new RestaurantCardHelper(id, restaurantName, restaurantType, imageUrl, rating));
                        adapter = new SearchAdapter(restaurants, searchListener);
                        allRatingsRecycler.setAdapter(adapter);
                    }
                }
                else {
                    noRatings.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void allRatingsOnClickListener() {

        searchListener = new SearchAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), Rating.class);
                intent.putExtra("id", restaurants.get(position).getId());
                startActivity(intent);
            }
        };

    }
}