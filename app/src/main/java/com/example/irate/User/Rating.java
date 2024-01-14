package com.example.irate.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.irate.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Rating extends AppCompatActivity {

    String id, userPhone, userId;
    ImageView restaurantImage, backButton;
    RatingBar ratingBar;
    TextView restaurantName, restaurantType, averageMealPrice, reporterName, dateTimeOfVisit, cleanlinessRating, serviceRating, foodQualityRating, notes;
    LinearLayout editDelete;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        SharedPreferences userSession = getSharedPreferences("userLoginSession", MODE_PRIVATE);
        userPhone = userSession.getString("phoneNumber", null);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
        }

        restaurantImage = findViewById(R.id.restaurant_rating_image);
        ratingBar = findViewById(R.id.restaurant_rating_rating_bar);
        restaurantName = findViewById(R.id.rating_screen_restaurant_name);
        restaurantType = findViewById(R.id.restaurant_rating_type);
        averageMealPrice = findViewById(R.id.restaurant_rating_average_price);
        reporterName = findViewById(R.id.restaurant_rating_reporter_name);
        dateTimeOfVisit = findViewById(R.id.restaurant_rating_date_of_visit);
        cleanlinessRating = findViewById(R.id.restaurant_rating_cleanliness);
        serviceRating = findViewById(R.id.restaurant_rating_service);
        foodQualityRating = findViewById(R.id.restaurant_rating_food_quality);
        notes = findViewById(R.id.restaurant_rating_notes);
        editDelete = findViewById(R.id.editdelete);
        backButton = findViewById(R.id.rating_back);
        Context context = this;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ratings").child(id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String url = snapshot.child("URL").getValue().toString();
                    Glide.with
                            (context)
                            .load(url)
                            .into(restaurantImage);
                    ratingBar.setRating(Float.parseFloat(snapshot.child("averageRating").getValue().toString())*-1);
                    restaurantName.setText(snapshot.child("restaurantName").getValue().toString());
                    restaurantType.setText(snapshot.child("restaurantType").getValue().toString());
                    averageMealPrice.setText(snapshot.child("averageMealPrice").getValue().toString());
                    reporterName.setText(snapshot.child("reporterName").getValue().toString());
                    dateTimeOfVisit.setText(snapshot.child("dateTimeOfVisit").getValue().toString());
                    cleanlinessRating.setText(getComponentRating(Float.parseFloat(snapshot.child("cleanlinessRating").getValue().toString())));
                    serviceRating.setText(getComponentRating(Float.parseFloat(snapshot.child("serviceRating").getValue().toString())));
                    foodQualityRating.setText(getComponentRating(Float.parseFloat(snapshot.child("foodQualityRating").getValue().toString())));
                    notes.setText(snapshot.child("notes").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Toast.makeText(Rating.this, userId, Toast.LENGTH_LONG).show();

    }

    private String getComponentRating(float componentRating) {

        int rating = (int) componentRating;
        String strRating = null;

        switch (rating) {
            case 1:
                strRating = "Needs to improve";
                break;
            case 2:
                strRating = "Okay";
                break;
            case 3:
                strRating = "Good";
                break;
            case 4:
                strRating = "Excellent";
                break;
        }

        return strRating;
    }

    public void callDeleteRating(View view) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ratings").child(id);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

        builder.setTitle("Delete Rating");
        builder.setMessage("Are you sure you want to delete this rating?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.removeValue();
                Intent intent = new Intent(Rating.this, Home.class);
                String deleted = "The rating has been deleted successfully";
                intent.putExtra("rating_deleted", deleted);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        builder.show();

    }

    public void callEditRating(View view) {

        Intent intent = new Intent(getApplicationContext(), EditRating.class);
        intent.putExtra("id", id);
        startActivity(intent);

    }
}