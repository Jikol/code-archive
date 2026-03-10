package com.orm;

public class Shift {

    private int id;
    private String arrivalTime;
    private String departureTime;
    private Employee employee;

    public void setId(int id) {
        this.id = id;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getId() {
        return id;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ID: " + this.id);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Arrival Time: " + this.arrivalTime);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Departure Time: " + this.departureTime);
        stringBuilder.append(System.getProperty("line.separator"));
        return stringBuilder.toString();
    }
}
