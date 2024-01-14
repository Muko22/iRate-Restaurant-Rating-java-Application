package com.example.irate.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.irate.HelperClass.HomeAdapter.HighestRatedAdapter;
import com.example.irate.HelperClass.HomeAdapter.RestaurantCardHelper;
import com.example.irate.HelperClass.SearchAdapter.SearchAdapter;
import com.example.irate.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    RecyclerView searchRecycler;
    RecyclerView.Adapter adapter;

    TextInputLayout search;
    TextInputEditText editSearch;

    ImageView backButton;

    private SearchAdapter.RecyclerViewClickListener searchListener;

    ArrayList<RestaurantCardHelper> restaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchRecycler = findViewById(R.id.search_recycler);
        search = findViewById(R.id.search);
        editSearch = findViewById(R.id.search_edit);
        backButton = findViewById(R.id.search_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String searchTerm = editSearch.getText().toString();
                    searchRecycler(searchTerm);
                    return true;
                }
                return false;
            }
        });
    }

    private void searchRecycler(String searchTerm) {

        setSearchOnClickListener();

        searchRecycler.setHasFixedSize(true);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ratings");

        String searchInputToLower = searchTerm.toString().toLowerCase();

        String searchInputTOUpper = searchTerm.toString().toUpperCase();

        Query firebaseSearchQuery = databaseReference.orderByChild("restaurantName").startAt(searchTerm).endAt(searchTerm + "\uf8ff");

        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String imageUrl = "https://i.picsum.photos/id/851/600/600.jpg?hmac=6cm2uCNhm0ILLLb5cX3VyyfS2XiYk6N_uuLo23wRAa0";

                if (snapshot.exists()) {
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
                        searchRecycler.setAdapter(adapter);
                    }
                }
                else {
                    //
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setSearchOnClickListener() {
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