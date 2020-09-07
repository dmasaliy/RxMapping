package com.example.rxmapping;

public class Motorcycle {

    private String brand;
    private Model model;
    public Motorcycle(String brand) {
        this.brand = brand;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public Model getModel() {
        return model;
    }
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Motorcycle{" +
                "brand='" + brand + '\'' +
                ", model=" + model +
                '}';
    }


    public Motorcycle(String brand, String model) {
        this.brand = brand;
        this.model = new Model(model);
    }
}
