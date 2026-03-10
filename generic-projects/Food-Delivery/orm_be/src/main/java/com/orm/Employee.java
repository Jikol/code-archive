package com.orm;

public class Employee {

    private int id;
    private String name = "";
    private String surname = "";
    private String gender = "";
    private String birthDate = "";
    private String accessionDate = "";
    private String withdrawalDate = "";
    private int hourlyWage;
    private Restaurant restaurant;
    private Employee foreman;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setAccessionDate(String accessionDate) {
        this.accessionDate = accessionDate;
    }

    public void setWithdrawalDate(String withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public void setHourlyWage(int hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setForeman(Employee foreman) {
        this.foreman = foreman;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getAccessionDate() {
        return accessionDate;
    }

    public String getWithdrawalDate() {
        return withdrawalDate;
    }

    public int getHourlyWage() {
        return hourlyWage;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Employee getForeman() {
        return foreman;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.name + " " + this.surname);
        return stringBuilder.toString();
    }
}
