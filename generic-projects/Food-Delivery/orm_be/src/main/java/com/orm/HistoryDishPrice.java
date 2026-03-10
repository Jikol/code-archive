package com.orm;

public class HistoryDishPrice {

    private int id;
    private int originalPrice;
    private String changeDate = "";
    private Dish dish;

    public void setId(int id) {
        this.id = id;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getId() {
        return id;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public Dish getDish() {
        return dish;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ID: " + this.id);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Original price: " + this.originalPrice);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Change date: " + this.changeDate);
        stringBuilder.append(System.getProperty("line.separator"));
        return stringBuilder.toString();
    }
}
