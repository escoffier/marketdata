package com.stock.model;

public class Address {
    String city;
    String country;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Address(String city, String country) {
        this.city = city;
        this.country = country;
    }
}
