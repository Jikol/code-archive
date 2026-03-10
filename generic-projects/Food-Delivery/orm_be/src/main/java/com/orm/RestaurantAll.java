package com.orm;

public class RestaurantAll extends Restaurant {

    private int employeeCount;
    private int vehicleCount;
    private int orderCount;

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }

    public void setVehicleCount(int vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public int getOrderCount() {
        return orderCount;
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
        stringBuilder.append("FoodType: " + this.foodType);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("DeliveryTime: " + this.deliveryTime);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Rating: " + this.rating);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("EmployeeCount: " + this.employeeCount);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("VehicleCount: " + this.vehicleCount);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("OrderCount: " + this.orderCount);
        stringBuilder.append(System.getProperty("line.separator"));
        return super.toString() + stringBuilder.toString();
        */
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.name);
        return stringBuilder.toString();
    }
}
