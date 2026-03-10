package com.orm;

public class Drive {

    private int id;
    private String rentalTime = "";
    private String returnTime = "";
    private Vehicle vehicle;
    private Employee employee;
    private String vehicleName = "";

    public void setId(int id) {
        this.id = id;
    }

    public void setRentalTime(String rentalTime) {
        this.rentalTime = rentalTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getId() {
        return id;
    }

    public String getRentalTime() {
        return rentalTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Employee getEmployee() {
        return employee;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ID: " + this.id);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Rental Time: " + this.rentalTime);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Return Time: " + this.returnTime);
        stringBuilder.append(System.getProperty("line.separator"));
        return stringBuilder.toString();
    }
}
