package org.example;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.example.PriceUtils.mergePrices;
import static org.junit.jupiter.api.Assertions.*;

class PriceUtilsTest {

    @Test
    @DisplayName("New price inside existing one with different value")
    public void testMergePrices_whenNewPriceIsInsideExistingPriceWithDifferentValue_shouldReturnThreePrices() {
        //Arrange
        List<Price> existingPrices = new ArrayList<>();
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 5, 0, 0, 0),
                new Date(123, 0, 20, 23, 59, 59), 11000));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 15, 23, 59, 59), 15000));

        List<Price> expected = new ArrayList<>();
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 5, 0, 0, 0),
                new Date(123, 0, 10, 0, 0, 0), 11000));
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 15, 23, 59, 59), 15000));
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 15, 23, 59, 59),
                new Date(123, 0, 20, 23, 59, 59), 11000));
        //Act
        Collection<Price> actual = mergePrices(existingPrices, newPrices);

        //Assert
        assertTrue(actual.size() == expected.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected), () -> "expected - " + expected + "  got " + actual +" instead");
    }

    @Test
    @DisplayName("New price intersects existing one with different value")
    public void testMergePrices_whenNewPriceIntersectsExistingPriceWithDifferentValue_shouldReturnTwoPrices() {
        //Arrange
        List<Price> existingPrices = new ArrayList<>();
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 20, 23, 59, 59), 11000));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, new Date(123, 0, 15, 0, 0, 0),
                new Date(123, 0, 25, 23, 59, 59), 5000));

        List<Price> expected = new ArrayList<>();
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 15, 0, 0, 0), 11000));
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 15, 0, 0, 0),
                new Date(123, 0, 25, 23, 59, 59), 5000));

        //Act
        Collection<Price> actual = mergePrices(existingPrices, newPrices);

        //Assert
        assertTrue(actual.size() == expected.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected), () -> "expected - " + expected + "  got " + actual +" instead");
    }

    @Test
    @DisplayName("New price between two existing with different values")
    public void testMergePrices_whenNewPriceIsBetweenTwoPricesExisting_shouldReturnThreePrices() {
        //Arrange
        List<Price> existingPrices = new ArrayList<>();
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 0, 0, 0, 0),
                new Date(123, 0, 10, 23, 59, 59), 11000));
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 10, 23, 59, 59),
                new Date(123, 0, 20, 23, 59, 59), 15000));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, new Date(123, 0, 5, 0, 0, 0),
                new Date(123, 0, 15, 23, 59, 59), 13000));


        List<Price> expected = new ArrayList<>();
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 0, 0, 0, 0),
                new Date(123, 0, 5, 0, 0, 0), 11000));
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 5, 0, 0, 0),
                new Date(123, 0, 15, 23, 59, 59), 13000));
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 15, 23, 59, 59),
                new Date(123, 0, 20, 23, 59, 59), 15000));

        //Act
        Collection<Price> actual = mergePrices(existingPrices, newPrices);

        //Assert
        assertTrue(actual.size() == expected.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected), () -> "expected - " + expected + "  got " + actual +" instead");
    }

    @Test
    @DisplayName("Two new prices between three existing")
    public void testMergePrices_whenTwoNewPricesBetweenThreeExistingPrices_ShouldReturnThreePrices() {
        //Arrange
        List<Price> existingPrices = new ArrayList<>();
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 5, 0, 0, 0),
                new Date(123, 0, 10, 23, 59, 59), 80));
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 10, 23, 59, 59),
                new Date(123, 0, 15, 23, 59, 59), 87));
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 15, 23, 59, 59),
                new Date(123, 0, 30, 23, 59, 59), 90));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, new Date(123, 0, 7, 0, 0, 0),
                new Date(123, 0, 13, 23, 59, 59), 80));
        newPrices.add(new Price("122856", 1, 1, new Date(123, 0, 13, 23, 59, 59),
                new Date(123, 0, 25, 23, 59, 59), 85));

        List<Price> expected = new ArrayList<>();
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 5, 0, 0, 0),
                new Date(123, 0, 13, 23, 59, 59), 80));
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 13, 23, 59, 59),
                new Date(123, 0, 25, 23, 59, 59), 85));
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 25, 23, 59, 59),
                new Date(123, 0, 30, 23, 59, 59), 90));

        //Act
        Collection<Price> actual = mergePrices(existingPrices, newPrices);

        //Assert
        assertTrue(actual.size() == expected.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected), () -> "expected - " + expected + "  got " + actual +" instead");
    }

    @Test
    @DisplayName("Two prices without intersection")
    public void testMergePrices_whenNewPriceHasNoIntersectionWithExistingPrice_ShouldReturnTwoPrices() {
        //Arrange
        List<Price> existingPrices = new ArrayList<>();
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 15, 23, 59, 59), 11000));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, new Date(123, 0, 20, 0, 0, 0),
                new Date(123, 0, 25, 23, 59, 59), 13000));

        List<Price> expected = new ArrayList<>();
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 15, 23, 59, 59), 11000));
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 20, 0, 0, 0),
                new Date(123, 0, 25, 23, 59, 59), 13000));

        //Act
        Collection<Price> actual = mergePrices(existingPrices, newPrices);

        //Assert
        assertTrue(actual.size() == expected.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected), () -> "expected - " + expected + "  got " + actual +" instead");
    }

    @Test
    @DisplayName("Two prices with different numbers")
    public void checkMergePrices_whenNewPricesHasAndExistingPriceHaveDifferentNumbers_ShouldReturnTwoPrices() {
        //Arrange
        List<Price> existingPrices = new ArrayList<>();
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 20, 23, 59, 59), 11000));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 2, 1, new Date(123, 0, 15, 0, 0, 0),
                new Date(123, 0, 25, 23, 59, 59), 13000));

        List<Price> expected = new ArrayList<>();
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 20, 23, 59, 59), 11000));
        expected.add(new Price("122856", 2, 1, new Date(123, 0, 15, 0, 0, 0),
                new Date(123, 0, 25, 23, 59, 59), 13000));

        //Act
        Collection<Price> actual = mergePrices(existingPrices, newPrices);

        //Assert
        assertTrue(actual.size() == expected.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected), () -> "expected - " + expected + "  got " + actual +" instead");
    }

    @Test
    @DisplayName("Two prices with different depart")
    public void checkMergePrices_whenNewPricesAndExistingPriceHaveDifferentDeparts_ShouldReturnTwoPrices() {
        //Arrange
        List<Price> existingPrices = new ArrayList<>();
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 20, 23, 59, 59), 11000));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 2, new Date(123, 0, 15, 0, 0, 0),
                new Date(123, 0, 25, 23, 59, 59), 13000));

        List<Price> expected = new ArrayList<>();
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 20, 23, 59, 59), 11000));
        expected.add(new Price("122856", 1, 2, new Date(123, 0, 15, 0, 0, 0),
                new Date(123, 0, 25, 23, 59, 59), 13000));

        //Act
        Collection<Price> actual = mergePrices(existingPrices, newPrices);

        //Assert
        assertTrue(actual.size() == expected.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected), () -> "expected - " + expected + "  got " + actual +" instead");
    }

    @Test
    @DisplayName("New price inside existing one with same value")
    public void testMergePrices_whenNewPriceIsInsideExistingPriceWithSameValue_shouldReturnOnePrice() {
        //Arrange
        List<Price> existingPrices = new ArrayList<>();
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 20, 23, 59, 59), 11000));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, new Date(123, 0, 15, 0, 0, 0),
                new Date(123, 0, 16, 23, 59, 59), 11000));

        List<Price> expected = new ArrayList<>();
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 20, 23, 59, 59), 11000));
        //Act
        Collection<Price> actual = mergePrices(existingPrices, newPrices);

        //Assert
        assertTrue(actual.size() == expected.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected), () -> "expected - " + expected + "  got " + actual +" instead");
    }

    @Test
    @DisplayName("Existing price is inside new one with same value")
    public void testMergePrices_whenExistingPriceIsInsideNewOneWithSameValue_shouldReturnOnePrice() {
        List<Price> existingPrices = new ArrayList<>();
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 15, 23, 59, 59), 5000));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, new Date(123, 0, 0, 0, 0, 0),
                new Date(123, 0, 30, 23, 59, 59), 5000));

        List<Price> expected = new ArrayList<>();
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 0, 0, 0, 0),
                new Date(123, 0, 30, 23, 59, 59), 5000));

        //Act
        Collection<Price> actual = mergePrices(existingPrices, newPrices);

        //Assert
        assertTrue(actual.size() == expected.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected), () -> "expected - " + expected + "  got " + actual +" instead");
    }

    @Test
    @DisplayName("Existing price inside new one with different value")
    public void testMergePrices_whenExistingPriceIsInsideNewOneWithDifferentValue_shouldReturnOnePrice() {
        List<Price> existingPrices = new ArrayList<>();
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 15, 23, 59, 59), 5000));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, new Date(123, 0, 5, 0, 0, 0),
                new Date(123, 0, 30, 23, 59, 59), 10000));

        List<Price> expected = new ArrayList<>();
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 5, 0, 0, 0),
                new Date(123, 0, 30, 23, 59, 59), 10000));
        //Act
        Collection<Price> actual = mergePrices(existingPrices, newPrices);

        //Assert
        assertTrue(actual.size() == expected.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected), () -> "expected - " + expected + "  got " + actual +" instead");

    }

    @Test
    @DisplayName("Extending existing price")
    public void testMergePrices_whenNewPriceIntersectsExistingPriceWithSameValue_shouldReturnExtendedPrice() {
        //Arrange
        List<Price> existingPrices = new ArrayList<>();
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 20, 23, 59, 59), 11000));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, new Date(123, 0, 15, 0, 0, 0),
                new Date(123, 0, 25, 23, 59, 59), 11000));

        List<Price> expected = new ArrayList<>();
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 25, 23, 59, 59), 11000));

        //Act
        Collection<Price> actual = mergePrices(existingPrices, newPrices);

        //Assert
        assertTrue(actual.size() == expected.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected), () -> "expected - " + expected + "  got " + actual +" instead");
    }


    @Test
    @DisplayName("New price's begin equals existing price's end")
    public void testMergePrices_whenNewPricesBeginEqualsToExistingPricesEndWithSameValue_shouldReturnOnePrice() {
        //Arrange
        List<Price> existingPrices = new ArrayList<>();
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 20, 23, 59, 59), 11000));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, new Date(123, 0, 20, 23, 59, 59),
                new Date(123, 0, 25, 23, 59, 59), 11000));

        List<Price> expected = new ArrayList<>();
        expected.add(new Price("122856", 1, 1, new Date(123, 0, 10, 0, 0, 0),
                new Date(123, 0, 25, 23, 59, 59), 11000));

        //Act
        Collection<Price> actual = mergePrices(existingPrices, newPrices);

        //Assert
        assertTrue(actual.size() == expected.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected), () -> "expected - " + expected + "  got " + actual +" instead");
    }

    @Test
    @DisplayName("Empty new prices")
    public void testMergePrices_whenNewPricesIsEmpty_shouldReturnExistingPrices() {
        //Arrange
        List<Price> existingPrices = new ArrayList<>();
        existingPrices.add(new Price("122856", 1, 1, new Date(123, 0, 20, 0, 0, 0),
                new Date(123, 1, 20, 23, 59, 59), 11000));
        existingPrices.add(new Price("122856", 2, 1, new Date(123, 0, 15, 0, 0, 0),
                new Date(123, 0, 25, 23, 59, 59), 92000));

        List<Price> newPrices = new ArrayList<>();
        List<Price> expected = new ArrayList<>(existingPrices);

        //Act
        Collection<Price> actual = mergePrices(existingPrices, newPrices);

        //Assert
        assertTrue(actual.size() == expected.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected), () -> "expected - " + expected + "  got " + actual +" instead");
    }

    @Test
    @DisplayName("Empty existing prices")
    public void testMergePrices_whenExistingPricesIsEmpty_shouldReturnNewPrices() {
        //Arrange
        List<Price> existingPrices = new ArrayList<>();

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, new Date(123, 0, 20, 0, 0, 0),
                new Date(123, 1, 20, 23, 59, 59), 11000));
        newPrices.add(new Price("122856", 2, 1, new Date(123, 0, 15, 0, 0, 0),
                new Date(123, 0, 25, 23, 59, 59), 92000));

        List<Price> expected = new ArrayList<>(newPrices);

        //Act
        Collection<Price> actual = mergePrices(existingPrices, newPrices);

        //Assert
        assertTrue(actual.size() == expected.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected), () -> "expected - " + expected + "  got " + actual +" instead");
    }
}