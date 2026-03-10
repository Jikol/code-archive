package com.orm;

public class Content {

    private int amount;
    private Order order;
    private Dish dish;

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public Order getOrder() {
        return order;
    }

    public Dish getDish() {
        return dish;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Amount: " + this.amount);
        stringBuilder.append(System.getProperty("line.separator"));
        return stringBuilder.toString();
    }
}
