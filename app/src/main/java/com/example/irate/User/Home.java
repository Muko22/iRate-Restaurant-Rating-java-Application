package com.example.irate.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.irate.Common.SignIn;
import com.example.irate.Databases.SessionManager;
import com.example.irate.HelperClass.HomeAdapter.RestaurantTypeCardAdapter;
import com.example.irate.HelperClass.HomeAdapter.RestaurantTypeCardHelper;
import com.example.irate.HelperClass.HomeAdapter.HighestRatedAdapter;
import com.example.irate.HelperClass.HomeAdapter.RecentlyAddedAdapter;
import com.example.irate.HelperClass.HomeAdapter.RestaurantCardHelper;
import com.example.irate.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Recycler Views
    RecyclerView highestRatedRecycler, recentlyAddedRecycler, restaurantTypeRecycler;
    RecyclerView.Adapter adapter;

    //Variables
    private GradientDrawable gradient1, gradient2, gradient3, gradient4;
    private FloatingActionButton addRating;
    private HighestRatedAdapter.RecyclerViewClickListener highestRatedListener;
    private RecentlyAddedAdapter.RecyclerViewClickListener recentlyAddedListener;
    private RestaurantTypeCardAdapter.RecyclerViewClickListener restaurantTypeListener;
    ImageView menuIcon;
    TextView highestRatedNoRatings, recentlyAddedNoRatings, fullName, email;

    // Card Helper Arrays
    ArrayList<RestaurantCardHelper> highestRatedRestaurants = new ArrayList<>();
    ArrayList<RestaurantCardHelper> recentlyAddedRestaurants = new ArrayList<>();
    ArrayList<RestaurantTypeCardHelper> restaurantTypes = new ArrayList<>();

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    Context context = Home.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String deleted = extras.getString("rating_deleted");
            Toast.makeText(Home.this, deleted, Toast.LENGTH_LONG).show();
            getIntent().removeExtra("rating_deleted");
        }

        SharedPreferences userSession = getSharedPreferences("userLoginSession", MODE_PRIVATE);
        String userFullName = userSession.getString("fullName", null);
        String userEmail = userSession.getString("email", null);

        // Hooks
        highestRatedRecycler = findViewById(R.id.highest_rated_recycler);
        recentlyAddedRecycler = findViewById(R.id.recently_added_recycler);
        restaurantTypeRecycler = findViewById(R.id.restaurant_type_recycler);
        addRating = findViewById(R.id.add_rating);
        menuIcon = findViewById(R.id.menu_icon);
        highestRatedNoRatings = findViewById(R.id.highest_rated_no_ratings);
        recentlyAddedNoRatings = findViewById(R.id.recently_added_no_ratings);

        // Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        fullName = findViewById(R.id.user_full_name);
        fullName.setText(userFullName);
        email = findViewById(R.id.user_email);
        email.setText(userEmail);

        navigationDrawer();

        // Recycler Views Function Calls
        highestRatedRecycler();
        recentlyAddedRecycler();
        restaurantTypeRecycler();

        addRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddRating();
            }
        });

    }

    // Navigation Drawer Functions

    private void navigationDrawer() {

        // Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int menuItemId = item.getItemId();

        if (menuItemId == R.id.nav_logout) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Clear shared preferences and logout

                    SessionManager sessionManager = new SessionManager(Home.this);
                    sessionManager.logout();

                    SharedPreferences signInScreen = getSharedPreferences("signInScreen", MODE_PRIVATE);
                    SharedPreferences.Editor editor = signInScreen.edit();
                    editor.putBoolean("firstTime", true);
                    editor.commit();

                    Intent intent = new Intent(context, SignIn.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Dismiss and return
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
        else if (menuItemId == R.id.nav_myratings) {
            Intent intent = new Intent(context, MyRatings.class);
            startActivity(intent);
        }
        else if (menuItemId == R.id.nav_allratings) {
            Intent intent = new Intent(context, AllRatings.class);
            startActivity(intent);
        }

        return true;

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    // Open 'Add Rating' Screen
    private void openAddRating() {
        Intent intent = new Intent(this, AddRating.class);
        startActivity(intent);
    }

    // Recycler Functions

    private void highestRatedRecycler() {

        setHighestRatedOnClickListener();

        highestRatedRecycler.setHasFixedSize(true);
        highestRatedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ratings");

        databaseReference.orderByChild("averageRating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String imageUrl = "https://i.picsum.photos/id/851/600/600.jpg?hmac=6cm2uCNhm0ILLLb5cX3VyyfS2XiYk6N_uuLo23wRAa0";

                if (snapshot.exists()) {
                    highestRatedNoRatings.setVisibility(View.INVISIBLE);
                    highestRatedRestaurants.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.getKey();
                        String restaurantName = ds.child("restaurantName").getValue().toString();
                        String restaurantType = ds.child("restaurantType").getValue().toString();
                        if (ds.child("URL").exists()) {
                            imageUrl = ds.child("URL").getValue().toString();
                        }
                        float rating = (Float.parseFloat(ds.child("averageRating").getValue().toString())) * -1;
                        highestRatedRestaurants.add(new RestaurantCardHelper(id, restaurantName, restaurantType, imageUrl, rating));
                        adapter = new HighestRatedAdapter(highestRatedRestaurants, highestRatedListener);
                        highestRatedRecycler.setAdapter(adapter);
                    }
                } else {
                    highestRatedNoRatings.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setHighestRatedOnClickListener() {
        highestRatedListener = new HighestRatedAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), Rating.class);
                intent.putExtra("id", highestRatedRestaurants.get(position).getId());
                startActivity(intent);
            }
        };
    }

    private void recentlyAddedRecycler() {

        setRecentlyAddedOnClickListener();

        recentlyAddedRecycler.setHasFixedSize(true);
        recentlyAddedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ratings");

        databaseReference.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String imageUrl = "https://i.picsum.photos/id/851/600/600.jpg?hmac=6cm2uCNhm0ILLLb5cX3VyyfS2XiYk6N_uuLo23wRAa0";

                if (snapshot.exists()) {
                    recentlyAddedNoRatings.setVisibility(View.INVISIBLE);
                    recentlyAddedRestaurants.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.getKey();
                        String restaurantName = ds.child("restaurantName").getValue().toString();
                        String restaurantType = ds.child("restaurantType").getValue().toString();
                        if (ds.child("URL").exists()) {
                            imageUrl = ds.child("URL").getValue().toString();
                        }
                        float rating = (Float.parseFloat(ds.child("averageRating").getValue().toString())) * -1;
                        recentlyAddedRestaurants.add(new RestaurantCardHelper(id, restaurantName, restaurantType, imageUrl, rating));
                        adapter = new RecentlyAddedAdapter(recentlyAddedRestaurants, recentlyAddedListener);
                        recentlyAddedRecycler.setAdapter(adapter);
                    }
                } else {
                    recentlyAddedNoRatings.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setRecentlyAddedOnClickListener() {
        recentlyAddedListener = new RecentlyAddedAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), Rating.class);
                intent.putExtra("id", recentlyAddedRestaurants.get(position).getId());
                startActivity(intent);
            }
        };
    }

    private void restaurantTypeRecycler() {

        setRestaurantTypeOnClickListener();

        restaurantTypeRecycler.setHasFixedSize(true);
        restaurantTypeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //All Gradients
        gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffd4cbe5, 0xffd4cbe5});
        gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7adccf, 0xff7adccf});
        gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7c59f, 0xFFf7c59f});
        gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8d7f5, 0xffb8d7f5});

        restaurantTypes.add(new RestaurantTypeCardHelper("Fine Dining", gradient1));
        restaurantTypes.add(new RestaurantTypeCardHelper("Casual Dining", gradient2));
        restaurantTypes.add(new RestaurantTypeCardHelper("Contemporary Casual", gradient3));
        restaurantTypes.add(new RestaurantTypeCardHelper("Family Style", gradient4));
        restaurantTypes.add(new RestaurantTypeCardHelper("Fast Food", gradient1));
        restaurantTypes.add(new RestaurantTypeCardHelper("Cafe", gradient2));
        restaurantTypes.add(new RestaurantTypeCardHelper("Buffet", gradient3));


        adapter = new RestaurantTypeCardAdapter(restaurantTypes, restaurantTypeListener);
        restaurantTypeRecycler.setAdapter(adapter);

    }

    private void setRestaurantTypeOnClickListener() {
        restaurantTypeListener = new RestaurantTypeCardAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), RestaurantType.class);
                intent.putExtra("restaurantType", restaurantTypes.get(position).getCategory());
                startActivity(intent);
            }
        };
    }

    // Open 'Search' Screen
    public void openSearch(View view) {
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

}