package com.example.irate.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.irate.R;
import com.example.irate.User.Home;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIMER = 5000;

    // Variables
    ImageView backgroundImage;
    TextView poweredBy;

    // Shared Preferences
    SharedPreferences signInScreen;

    // Animations
    Animation sideAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        // Hooks
        backgroundImage = findViewById(R.id.background_image);
        poweredBy = findViewById(R.id.powered_by);

        // Animations
        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        // set Animations on elements
        backgroundImage.setAnimation(sideAnim);
        poweredBy.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                signInScreen = getSharedPreferences("signInScreen", MODE_PRIVATE);
                boolean isFirstTime = signInScreen.getBoolean("firstTime", true);

                if (isFirstTime) {

                    Intent intent = new Intent(SplashScreen.this, SignIn.class);
                    startActivity(intent);
                    finish();

                } else {

                    Intent intent = new Intent(SplashScreen.this, Home.class);
                    startActivity(intent);
                    finish();

                }

            }
        }, SPLASH_TIMER);
    }
}