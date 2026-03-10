package com.orm;

public class Dish {

    private int id;
    private String name = "";
    private int weight;
    private int price;
    private Restaurant restaurant;
    private Content content;
    private String amount = "0";

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public int getPrice() {
        return price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Content getContent() {
        return content;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ID: " + this.id);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Name: " + this.name);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Weight: " + this.weight);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Price: " + this.price);
        stringBuilder.append(System.getProperty("line.separator"));
        return stringBuilder.toString();
    }
}
