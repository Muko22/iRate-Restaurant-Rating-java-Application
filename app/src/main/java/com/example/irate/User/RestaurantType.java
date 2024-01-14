package com.example.irate.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
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

public class RestaurantType extends AppCompatActivity {

    String restaurantType;
    RecyclerView typeRatingsRecycler;
    RecyclerView.Adapter adapter;
    private SearchAdapter.RecyclerViewClickListener searchListener;
    ArrayList<RestaurantCardHelper> restaurants = new ArrayList<>();
    TextView noRatings, restaurantTypeHeader;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_type);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            restaurantType = extras.getString("restaurantType");
        }

        // Hooks
        typeRatingsRecycler = findViewById(R.id.restaurant_type_ratings_recycler);
        noRatings = findViewById(R.id.restaurant_type_ratings_no_ratings);
        restaurantTypeHeader = findViewById(R.id.restaurant_type_header);
        backButton = findViewById(R.id.restaurant_type_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        restaurantTypeHeader.setText(restaurantType);

        // Call Recycler Function
        typeRatingsRecycler();

    }

    private void typeRatingsRecycler() {

        typeRatingsOnClickListener();

        typeRatingsRecycler.setHasFixedSize(true);
        typeRatingsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ratings");

        Query firebaseSearchQuery = databaseReference.orderByChild("restaurantType").equalTo(restaurantType);

        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
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
                        typeRatingsRecycler.setAdapter(adapter);
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

    private void typeRatingsOnClickListener() {
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