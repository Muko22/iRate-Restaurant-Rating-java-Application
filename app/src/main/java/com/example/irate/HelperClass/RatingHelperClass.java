package com.example.irate.HelperClass;

import java.time.LocalDateTime;

public class RatingHelperClass {

    String restaurantName, restaurantType, dateTimeOfVisit, notes, reporterName, userId;
    float averageMealPrice, cleanlinessRating, serviceRating, foodQualityRating, averageRating;
    int timeStamp;

    public RatingHelperClass(String restaurantName, String restaurantType, String dateTimeOfVisit, String notes, String reporterName, String userId, float averageMealPrice, float cleanlinessRating, float serviceRating, float foodQualityRating, float averageRating, int timeStamp) {
        this.restaurantName = restaurantName;
        this.restaurantType = restaurantType;
        this.dateTimeOfVisit = dateTimeOfVisit;
        this.notes = notes;
        this.reporterName = reporterName;
        this.userId = userId;
        this.averageMealPrice = averageMealPrice;
        this.cleanlinessRating = cleanlinessRating;
        this.serviceRating = serviceRating;
        this.foodQualityRating = foodQualityRating;
        this.averageRating = averageRating;
        this.timeStamp = timeStamp;
    }

    public RatingHelperClass() {
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(String restaurantType) {
        this.restaurantType = restaurantType;
    }

    public String getDateTimeOfVisit() {
        return dateTimeOfVisit;
    }

    public void setDateTimeOfVisit(String dateTimeOfVisit) {
        this.dateTimeOfVisit = dateTimeOfVisit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getAverageMealPrice() {
        return averageMealPrice;
    }

    public void setAverageMealPrice(float averageMealPrice) {
        this.averageMealPrice = averageMealPrice;
    }

    public float getCleanlinessRating() {
        return cleanlinessRating;
    }

    public void setCleanlinessRating(float cleanlinessRating) {
        this.cleanlinessRating = cleanlinessRating;
    }

    public float getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(float serviceRating) {
        this.serviceRating = serviceRating;
    }

    public float getFoodQualityRating() {
        return foodQualityRating;
    }

    public void setFoodQualityRating(float foodQualityRating) {
        this.foodQualityRating = foodQualityRating;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }
}
