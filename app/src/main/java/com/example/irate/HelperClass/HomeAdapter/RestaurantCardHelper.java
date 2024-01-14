package com.example.irate.HelperClass.HomeAdapter;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.RatingBar;

import com.bumptech.glide.load.model.GlideUrl;

public class RestaurantCardHelper {

    String id ,name, category, url;
    float rating;

    public RestaurantCardHelper(String id, String name, String category, String url, float rating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.url = url;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
