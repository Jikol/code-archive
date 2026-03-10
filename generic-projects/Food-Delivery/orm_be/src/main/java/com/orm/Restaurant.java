package com.orm;

public class Restaurant {

    protected int id;
    protected String name = "";
    protected String desc = "";
    protected String foodType = "";
    protected int deliveryTime;
    protected int rating;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public void setRating(int rating) { this.rating = rating; }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getFoodType() {
        return foodType;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public String toString() {
        /*
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("----------------------------------");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("ID: " + this.id);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Name: " + this.name);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Desc: " + this.desc);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Food Type: " + this.foodType);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Delivery Time: " + this.deliveryTime);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Rating: " + this.rating);
        stringBuilder.append(System.getProperty("line.separator"));
        return stringBuilder.toString();
         */
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.name);
        return stringBuilder.toString();
    }
}
