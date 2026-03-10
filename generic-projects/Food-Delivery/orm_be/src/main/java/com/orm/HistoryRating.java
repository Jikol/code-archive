package com.orm;

public class HistoryRating {

    private int id;
    private int rating;
    private String changeDate = "";
    private Restaurant restaurant;

    public void setId(int id) {
        this.id = id;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public Restaurant getRestaurant() {
        return restaurant;
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
        stringBuilder.append("ChangeDate: " + this.changeDate);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Restaurant ID: " + this.restaurant.getId());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Restaurant Name: " + this.restaurant.getName());
        stringBuilder.append(System.getProperty("line.separator"));
        return stringBuilder.toString();
    }
}
