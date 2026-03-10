package com.orm;

public class Address {

    private int id;
    private String street = "";
    private String streetNumber = "";
    private String city = "";
    private int zip;

    public void setId(int id) {
        this.id = id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public int getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getCity() {
        return city;
    }

    public int getZip() {
        return zip;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("----------------------------------");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("ID: " + this.id);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Street: " + this.street);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Street Number: " + this.streetNumber);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("City: " + this.city);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Zip: " + this.zip);
        stringBuilder.append(System.getProperty("line.separator"));
        return stringBuilder.toString();
    }
}
