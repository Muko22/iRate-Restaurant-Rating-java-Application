package com.example.irate.HelperClass.HomeAdapter;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.widget.RelativeLayout;

public class RestaurantTypeCardHelper {

    String category;
    GradientDrawable gradient;

    public RestaurantTypeCardHelper(String category, GradientDrawable gradient) {
        this.category = category;
        this.gradient = gradient;
    }

    public String getCategory() {
        return category;
    }

    public GradientDrawable getGradient() {
        return gradient;
    }
}
