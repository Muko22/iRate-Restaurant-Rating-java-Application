package com.example.irate.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TimePicker;
import android.widget.Toast;

//import com.example.irate.DialogImageChooser;
import com.example.irate.HelperClass.RatingHelperClass;
import com.example.irate.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AddRating extends AppCompatActivity {

    private RatingBar cleanlinessRatingBar, serviceRatingBar, foodQualityRatingBar;
    private TextInputLayout restaurantName, restaurantType, dateTimeOfVisit, averageMealPrice, notes, reporterName;
    private TextInputEditText editRestaurantName, editDateTimeOfVisit, editAverageMealPrice, editNotes, editReporterName;
    private AutoCompleteTextView autoRestaurantType;
    private ImageView restaurantImage, backButton;
    private Uri imageUri;


    private String strCleanlinessRating, downloadUrl;
    private float cleanlinessRating = 0;

    private String strServiceRating;
    private float serviceRating = 0;

    private String strFoodQualityRating;
    private float foodQualityRating = 0;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private SharedPreferences sp;
    int maxid = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rating);

        // Hooks
        restaurantName = findViewById(R.id.restaurant_name);
        restaurantName = findViewById(R.id.restaurant_name);
        editRestaurantName = findViewById(R.id.restaurant_name_edit);
        restaurantType = findViewById(R.id.restaurant_type);
        autoRestaurantType = findViewById(R.id.restaurant_type_auto);
        dateTimeOfVisit = findViewById(R.id.date_time);
        editDateTimeOfVisit = findViewById(R.id.date_time_edit);
        averageMealPrice = findViewById(R.id.average_meal_price);
        editAverageMealPrice = findViewById(R.id.average_meal_price_edit);
        cleanlinessRatingBar = findViewById(R.id.cleanliness_rating);
        serviceRatingBar = findViewById(R.id.service_rating);
        foodQualityRatingBar = findViewById(R.id.food_quality_rating);
        reporterName = findViewById(R.id.reporter_name);
        editReporterName = findViewById(R.id.reporter_name_edit);
        notes = findViewById(R.id.notes);
        editNotes = findViewById(R.id.notes_edit);
        restaurantImage = findViewById(R.id.restaurant_image);
        backButton = findViewById(R.id.add_rating_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        editDateTimeOfVisit.setInputType(InputType.TYPE_NULL);

        editDateTimeOfVisit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDateTimeDialog(editDateTimeOfVisit);
            }
        });

        String[] option = {"Fine Dining", "Casual Dining", "Contemporary Casual", "Family Style", "Fast Food", "Cafe", "Buffet"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.restaurant_type_dropdown, option);
        autoRestaurantType.setText(arrayAdapter.getItem(0).toString(), false);
        autoRestaurantType.setAdapter(arrayAdapter);

        restaurantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        cleanlinessRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                int rating = (int) v;
                String message = null;

                cleanlinessRating = ratingBar.getRating();

                switch (rating) {
                    case 1:
                        message = "Needs to improve";
                        strCleanlinessRating = "Needs to improve";
                        break;
                    case 2:
                        message = "Okay";
                        strCleanlinessRating = "Okay";
                        break;
                    case 3:
                        message = "Good";
                        strCleanlinessRating = "Good";
                        break;
                    case 4:
                        message = "Excellent";
                        strCleanlinessRating = "Excellent";
                        break;
                }

                Toast.makeText(AddRating.this, message, Toast.LENGTH_SHORT).show();

            }
        });

        serviceRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                int rating = (int) v;
                String message = null;

                serviceRating = ratingBar.getRating();

                switch (rating) {
                    case 1:
                        message = "Needs to improve";
                        strServiceRating = "Needs to improve";
                        break;
                    case 2:
                        message = "Okay";
                        strServiceRating = "Okay";
                        break;
                    case 3:
                        message = "Good";
                        strServiceRating = "Good";
                        break;
                    case 4:
                        message = "Excellent";
                        strServiceRating = "Excellent";
                        break;
                }

                Toast.makeText(AddRating.this, message, Toast.LENGTH_SHORT).show();

            }
        });

        foodQualityRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                int rating = (int) v;
                String message = null;

                foodQualityRating = ratingBar.getRating();

                switch (rating) {
                    case 1:
                        message = "Needs to improve";
                        strFoodQualityRating = "Needs to improve";
                        break;
                    case 2:
                        message = "Okay";
                        strFoodQualityRating = "Okay";
                        break;
                    case 3:
                        message = "Good";
                        strFoodQualityRating = "Good";
                        break;
                    case 4:
                        message = "Excellent";
                        strFoodQualityRating = "Excellent";
                        break;
                }

                Toast.makeText(AddRating.this, message, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null & data.getData()!=null) {
            imageUri = data.getData();
            restaurantImage.setImageURI(imageUri);
        }
    }

    private void uploadImage() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Adding rating...");
        pd.show();

        if (imageUri != null && !imageUri.equals(imageUri.EMPTY)) {

            String strId = String.valueOf(maxid);
            StorageReference imageRef = storageReference.child("images/" + strId);

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pd.dismiss();
                            downloadUrl = uri.toString();
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("ratings");
                            reference.child(String.valueOf(maxid)).child("URL").setValue(downloadUrl);
                            Toast.makeText(AddRating.this, "Your rating has been added successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(AddRating.this, "There was a problem adding your rating, please try again later", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Progress: " + (int) progressPercent + "%");
                }
            });
        }
        else {
            String strId = String.valueOf(maxid);
            StorageReference imageRef = storageReference.child("images/" + strId);

            Bitmap bmp = null;
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_restaurant_image);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pd.dismiss();
                            downloadUrl = uri.toString();
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("ratings");
                            reference.child(String.valueOf(maxid)).child("URL").setValue(downloadUrl);
                            Toast.makeText(AddRating.this, "Your rating has been added successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(AddRating.this, "There was a problem adding your rating, please try again later", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Progress: " + (int) progressPercent + "%");
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDateTimeDialog(TextInputEditText editDateTimeOfVisit) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");

                        editDateTimeOfVisit.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(AddRating.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(AddRating.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean validateRestaurantName() {

        String val = restaurantName.getEditText().getText().toString();

        if (val.isEmpty()) {
            restaurantName.setError("This field cannot be empty");
            editRestaurantName.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editRestaurantName, InputMethodManager.SHOW_IMPLICIT);
            return false;
        } else {
            restaurantName.setError(null);
            restaurantName.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateRestaurantType() {

        String val = restaurantType.getEditText().getText().toString();

        if (val.isEmpty()) {
            restaurantType.setError("This field cannot be empty");
            return false;
        } else {
            restaurantType.setError(null);
            restaurantType.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateAverageMealPrice() {

        String val = averageMealPrice.getEditText().getText().toString();

        if (val.isEmpty()) {
            averageMealPrice.setError("This field cannot be empty");
            editAverageMealPrice.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editAverageMealPrice, InputMethodManager.SHOW_IMPLICIT);
            return false;
        } else {
            averageMealPrice.setError(null);
            averageMealPrice.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateDateTimeOfVisit() {

        String val = dateTimeOfVisit.getEditText().getText().toString();

        if (val.isEmpty()) {
            dateTimeOfVisit.setError("This field cannot be empty");
            editDateTimeOfVisit.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editDateTimeOfVisit, InputMethodManager.SHOW_IMPLICIT);
            return false;
        } else {
            dateTimeOfVisit.setError(null);
            dateTimeOfVisit.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateCleanlinessRating() {

        if (cleanlinessRating == 0) {
            Toast.makeText(this, "Please give a cleanliness rating", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    private boolean validateServiceRating() {

        if (serviceRating == 0) {
            Toast.makeText(this, "Please give a service rating", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    private boolean validateFoodQualityRating() {

        if (foodQualityRating == 0) {
            Toast.makeText(this, "Please give a food quality rating", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    private boolean validateReporterName() {

        String val = reporterName.getEditText().getText().toString();
        String validName = "[a-zA-Z ]+";

        if (val.isEmpty()) {
            reporterName.setError("This field cannot be empty");
            editReporterName.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editReporterName, InputMethodManager.SHOW_IMPLICIT);
            return false;
        } else if (!val.matches(validName)) {
            reporterName.setError("Please enter a valid reporter name. Use letters only.");
            editReporterName.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editReporterName, InputMethodManager.SHOW_IMPLICIT);
            return false;
        } else {
            reporterName.setError(null);
            reporterName.setErrorEnabled(false);
            return true;
        }

    }

    public void callSubmit(View view) {

        if (!validateRestaurantName()) {
            return;
        }
        else if (!validateRestaurantType()) {
            return;
        }
        else if (!validateDateTimeOfVisit()) {
            return;
        }
        else if (!validateAverageMealPrice()) {
            return;
        }
        else if (!validateCleanlinessRating()) {
            return;
        }
        else if (!validateServiceRating()) {
            return;
        }
        else if (!validateFoodQualityRating()) {
            return;
        }
        else if (!validateReporterName()) {
            return;
        }

        String strRestaurantName = restaurantName.getEditText().getText().toString();
        String strRestaurantType = restaurantType.getEditText().getText().toString();
        String strDateTimeOfVisit = dateTimeOfVisit.getEditText().getText().toString();
        String strAverageMealPrice = averageMealPrice.getEditText().getText().toString();
        float averagePrice = Float.parseFloat(strAverageMealPrice);

        float averageRating = (cleanlinessRating + serviceRating + foodQualityRating)/-3;

        String strNotes = notes.getEditText().getText().toString();
        String strReporterName = reporterName.getEditText().getText().toString();

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

        builder.setTitle("Submit Rating?");
        builder.setMessage("Restaurant Name -> "+strRestaurantName+ "\n\n" +
                "Restaurant Type -> "+strRestaurantType+ "\n\n" +
                "Date and Time of Visit -> "+strDateTimeOfVisit+ "\n\n" +
                "Average Price of Meal -> K"+strAverageMealPrice+ "\n\n" +
                "Cleanliness Rating -> "+strCleanlinessRating+ "\n\n" +
                "Service Rating -> "+strServiceRating+ "\n\n" +
                "Food Quality Rating -> "+strFoodQualityRating+ "\n\n" +
                "Notes -> "+strNotes+ "\n\n" +
                "Reporter Name -> "+strReporterName);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(DialogInterface dialog, int which) {

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference().child("ratings");

                int timeStamp = ((int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()))*-1;

                maxid = timeStamp * -1;

                SharedPreferences userSession = getSharedPreferences("userLoginSession", MODE_PRIVATE);
                String userId = userSession.getString("phoneNumber", null);

                RatingHelperClass ratingHelperClass = new RatingHelperClass(strRestaurantName, strRestaurantType, strDateTimeOfVisit, strNotes, strReporterName, userId, averagePrice, cleanlinessRating, serviceRating, foodQualityRating, averageRating, timeStamp);

                reference.child(String.valueOf(maxid)).setValue(ratingHelperClass);

                uploadImage();

                dialog.dismiss();
                editRestaurantName.setText("");
                editDateTimeOfVisit.setText("");
                editAverageMealPrice.setText("");
                editNotes.setText("");
                editReporterName.setText("");
                cleanlinessRatingBar.setRating(0);
                serviceRatingBar.setRating(0);
                foodQualityRatingBar.setRating(0);
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

}