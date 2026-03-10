package com.orm;

public class Rating {

    private int id;
    private int rating;
    private String ratingDate = "";
    private String ratingComment = "";
    private Restaurant restaurant;
    private Customer customer;

    public void setId(int id) {
        this.id = id;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setRatingDate(String ratingDate) {
        this.ratingDate = ratingDate;
    }

    public void setRatingComment(String ratingComment) {
        this.ratingComment = ratingComment;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getRatingDate() {
        return ratingDate;
    }

    public String getRatingComment() {
        return ratingComment;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("----------------------------------");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("ID: " + this.id);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Rating: " + this.rating);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Rating Date: " + this.ratingDate);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Rating Comment: " + this.ratingComment);
        stringBuilder.append(System.getProperty("line.separator"));
        return stringBuilder.toString();
    }
}
