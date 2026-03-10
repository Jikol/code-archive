package com.orm;

public class EmployeeAll extends Employee {

    private double hoursWorked;

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
