package com.example.irate.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.irate.Databases.SessionManager;
import com.example.irate.R;
import com.example.irate.User.Home;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    private TextInputLayout phoneNumber, password;
    private TextInputEditText editPhoneNumber, editPassword;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String successMessage = extras.getString("user_signed_up");
            Toast.makeText(SignIn.this, successMessage, Toast.LENGTH_LONG).show();
        }

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("users");

        phoneNumber = findViewById(R.id.sign_in_phone_number);
        editPhoneNumber = findViewById(R.id.sign_in_phone_number_edit);
        password = findViewById(R.id.sign_in_password);
        editPassword = findViewById(R.id.sign_in_password_edit);

    }

    public void openSignUp(View view) {

        Intent intent = new Intent(SignIn.this, SignUp.class);;
        startActivity(intent);

    }

    private boolean validatePhoneNumber() {

        String val = phoneNumber.getEditText().getText().toString();

        if (val.isEmpty()) {
            phoneNumber.setError("This field cannot be empty");
            phoneNumber.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editPhoneNumber, InputMethodManager.SHOW_IMPLICIT);
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePassword() {

        String val = password.getEditText().getText().toString();

        if (val.isEmpty()) {
            password.setError("This field cannot be empty");
            password.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editPassword, InputMethodManager.SHOW_IMPLICIT);
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }

    public void signIn(View view) {

        if (!validatePhoneNumber()) {
            return;
        }
        else if (!validatePassword()) {
            return;
        }

        // Get data
        String _phoneNumber = phoneNumber.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();

        Query checkUser = reference.orderByChild("phoneNumber").equalTo(_phoneNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);

                    String systemPassword = snapshot.child(_phoneNumber).child("password").getValue(String.class);
                    if (systemPassword.equals(_password)) {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        String _fullname = snapshot.child(_phoneNumber).child("fullName").getValue(String.class);
                        String _email = snapshot.child(_phoneNumber).child("email").getValue(String.class);

                        // Create a session

                        SessionManager sessionManager = new SessionManager(SignIn.this);
                        sessionManager.createLoginSession(_fullname, _email, _phoneNumber);

                        SharedPreferences signInScreen = getSharedPreferences("signInScreen", MODE_PRIVATE);
                        SharedPreferences.Editor editor = signInScreen.edit();
                        editor.putBoolean("firstTime", false);
                        editor.commit();

                        Intent intent = new Intent(SignIn.this, Home.class);;
                        startActivity(intent);

                    }
                    else {
                        Toast.makeText(SignIn.this, "The password you entered is wrong", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(SignIn.this, "This user does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignIn.this, "There was problem signing in, please try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }
}