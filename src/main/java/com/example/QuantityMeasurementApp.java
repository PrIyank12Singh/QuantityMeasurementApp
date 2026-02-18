package com.example;

public class QuantityMeasurementApp {

    // Common base class
    public static abstract class Measurement {
        protected final double value;

        protected Measurement(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if (obj == null || getClass() != obj.getClass())
                return false;

            Measurement other = (Measurement) obj;
            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }

    public static class Feet extends Measurement {
        public Feet(double value) {
            super(value);
        }
    }

    public static class Inches extends Measurement {
        public Inches(double value) {
            super(value);
        }
    }

    public static boolean checkFeetEquality(double value1, double value2) {
        return new Feet(value1).equals(new Feet(value2));
    }

    public static boolean checkInchesEquality(double value1, double value2) {
        return new Inches(value1).equals(new Inches(value2));
    }

    public static void main(String[] args) {
        System.out.println("=== Feet Comparisons ===");
        System.out.println(checkFeetEquality(1.0, 1.0));
        System.out.println(checkFeetEquality(1.0, 2.0));

        System.out.println("\n=== Inches Comparisons ===");
        System.out.println(checkInchesEquality(1.0, 1.0));
        System.out.println(checkInchesEquality(1.0, 2.0));
    }
}
