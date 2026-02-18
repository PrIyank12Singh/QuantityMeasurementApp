package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FeetTest {

    @Test
    @DisplayName("Same values should be equal")
    void testEquality_SameValue() {
        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(1.0);
        assertEquals(feet1, feet2);
    }

    @Test
    @DisplayName("Different values should not be equal")
    void testEquality_DifferentValue() {
        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(2.0);
        assertNotEquals(feet1, feet2);
    }

    @Test
    @DisplayName("Null comparison should return false")
    void testEquality_NullComparison() {
        Feet feet1 = new Feet(1.0);
        assertNotEquals(feet1, null);
    }

    @Test
    @DisplayName("Different object type should return false")
    void testEquality_DifferentType() {
        Feet feet1 = new Feet(1.0);
        String value = "1.0";
        assertNotEquals(feet1, value);
    }

    @Test
    @DisplayName("Reflexive property")
    void testEquality_Reflexive() {
        Feet feet = new Feet(3.0);
        assertEquals(feet, feet);
    }

    @Test
    @DisplayName("Symmetric property")
    void testEquality_Symmetric() {
        Feet f1 = new Feet(5.0);
        Feet f2 = new Feet(5.0);
        assertEquals(f1, f2);
        assertEquals(f2, f1);
    }

    @Test
    @DisplayName("Transitive property")
    void testEquality_Transitive() {
        Feet f1 = new Feet(7.0);
        Feet f2 = new Feet(7.0);
        Feet f3 = new Feet(7.0);
        assertEquals(f1, f2);
        assertEquals(f2, f3);
        assertEquals(f1, f3);
    }
}
