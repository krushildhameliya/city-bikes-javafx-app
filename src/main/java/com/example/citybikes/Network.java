package com.example.citybikes;

public class Network {
    private String id;
    private String name;
    private String city;

    public Network(String id, String name, String city ) {
        this.id = id;
        this.name = name;
        this.city = city;

    }

    // Setters and Getters for id, name, city, and description
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



    @Override
    public String toString() {
        return "Bike Name: " + name + "\n"
                + "City: " + city + "\n";
    }


}
