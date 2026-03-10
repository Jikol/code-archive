package com.orm;

public class Customer {

    private int id;
    private String name = "";
    private String surname = "";
    private String gender = "";
    private String phoneNumber = "";
    private String birthDate = "";
    private Address address;

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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("----------------------------------");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("ID: " + this.id);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Name: " + this.name);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Surname: " + this.surname);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Gender: " + this.gender);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Phone Number: " + this.phoneNumber);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Birth Date: " + this.birthDate);
        stringBuilder.append(System.getProperty("line.separator"));
        return stringBuilder.toString();
    }
}
