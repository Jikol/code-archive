package com.orm;

import javafx.beans.value.ObservableValue;

public class Order {

    private int id;
    private String creationTime = "";
    private String handoverTime = "";
    private String deliveryTime = "";
    private String paymentType = "";
    private int tip;
    private Restaurant restaurant;
    private Employee employee;
    private Customer customer;

    public void setId(int id) {
        this.id = id;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public void setHandoverTime(String handoverTime) {
        this.handoverTime = handoverTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getHandoverTime() {
        if (handoverTime.equals("")) {
            return "neprovedeno";
        } else {
            return handoverTime;
        }
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public int getTip() {
        return tip;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getRestaurantName() {
        return restaurant.getName();
    }

    public int getRestaurantId() {
        return restaurant.getId();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ID: " + this.id);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Creation time: " + this.creationTime);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Handover time: " + this.handoverTime);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Delivery time: " + this.deliveryTime);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Payment type: " + this.paymentType);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Tip: " + this.tip);
        stringBuilder.append(System.getProperty("line.separator"));
        return stringBuilder.toString();
    }
}
