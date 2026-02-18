package com.example;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(1.0);
        Feet feet3 = new Feet(2.0);

        System.out.println("Comparing 1.0 ft and 1.0 ft: " + feet1.equals(feet2));
        System.out.println("Comparing 1.0 ft and 2.0 ft: " + feet1.equals(feet3));
        System.out.println("Comparing 1.0 ft with null: " + feet1.equals(null));
        System.out.println("Comparing 1.0 ft with itself: " + feet1.equals(feet1));
    }
}
