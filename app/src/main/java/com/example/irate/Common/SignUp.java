package com.example.irate.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.irate.HelperClass.UserHelperClass;
import com.example.irate.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private TextInputLayout fullname, email, phoneNumber, password;
    private TextInputEditText editFullName, editEmail, editPhoneNumber, editPassword;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("users");

        fullname = findViewById(R.id.full_name);
        editFullName = findViewById(R.id.full_name_edit);
        email = findViewById(R.id.email_address);
        editEmail = findViewById(R.id.email_address_edit);
        phoneNumber = findViewById(R.id.phone_number);
        editPhoneNumber = findViewById(R.id.phone_number_edit);
        password = findViewById(R.id.password);
        editPassword = findViewById(R.id.password_edit);

    }

    private boolean validateFullName() {

        String val = fullname.getEditText().getText().toString();

        if (val.isEmpty()) {
            fullname.setError("This field cannot be empty");
            fullname.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editFullName, InputMethodManager.SHOW_IMPLICIT);
            return false;
        } else {
            fullname.setError(null);
            fullname.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateEmail() {

        String val = email.getEditText().getText().toString();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("This field cannot be empty");
            email.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editEmail, InputMethodManager.SHOW_IMPLICIT);
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("This email address is invalid");
            email.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(email, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePassword() {

        String val = password.getEditText().getText().toString();
        String checkPassword = "^" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%&+=])" +
                "(?=\\S+$)" +
                ".{6,}" +
                "$";

        if (val.isEmpty()) {
            password.setError("This field cannot be empty");
            password.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editPassword, InputMethodManager.SHOW_IMPLICIT);
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError("This password is invalid. Your password should contain at least 6 characters, on special character and should not contain any whitespaces");
            password.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editPassword, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

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

    public void signUp(View view) {

        if (!validateFullName()) {
            return;
        }
        else if (!validateEmail()) {
            return;
        }
        else if (!validatePassword()) {
            return;
        }

        UserHelperClass userHelperClass = new UserHelperClass(fullname.getEditText().getText().toString(), email.getEditText().getText().toString(), phoneNumber.getEditText().getText().toString(), password.getEditText().getText().toString());

        reference.child(phoneNumber.getEditText().getText().toString()).setValue(userHelperClass);

        Intent intent = new Intent(SignUp.this, SignIn.class);
        String signup = "Your sign up was successful";
        intent.putExtra("user_signed_up", signup);
        startActivity(intent);

    }

    public void openSignIn(View view) {

        Intent intent = new Intent(SignUp.this, SignIn.class);;
        startActivity(intent);

    }
}