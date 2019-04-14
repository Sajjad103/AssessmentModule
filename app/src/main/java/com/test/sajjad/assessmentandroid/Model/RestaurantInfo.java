package com.test.sajjad.assessmentandroid.Model;

public class RestaurantInfo {

    public String image;
    public String name;
    public String rating;
    public String reviews;

    public RestaurantInfo(String image, String name, String rating, String reviews) {
        this.image = image;
        this.name = name;
        this.rating = rating;
        this.reviews = reviews;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }
}
