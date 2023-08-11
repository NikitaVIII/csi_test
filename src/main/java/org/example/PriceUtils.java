package org.example;


import java.util.*;
import java.util.stream.Collectors;

public class PriceUtils {

    /**
     *
     * @param existingPrices - collection of existing prices
     * @param newPrices      - collection of new prices
     * @return - collection of merged prices
     */
    public static Collection<Price> mergePrices(Collection<Price> existingPrices, Collection<Price> newPrices) {
        Collection<Price> mergedPrices = new HashSet<>();

        //Grouping by composite keys
        Map<CompositeKey, Set<Price>> existingPricesMap = getPricesMap(existingPrices);
        Map<CompositeKey, Set<Price>> newPricesMap = getPricesMap(newPrices);

        Set<CompositeKey> existingKeys = new HashSet<>(existingPricesMap.keySet());
        Set<CompositeKey> newKeys = new HashSet<>(newPricesMap.keySet());

        Set<CompositeKey> intersection = new HashSet<>(existingKeys);
        intersection.retainAll(newKeys);

        //We should only merge prices with common keys
        for (CompositeKey key : intersection) {
            Set<Price> exPrices = existingPricesMap.get(key);
            for (Price newPrice : newPricesMap.get(key)) {
                exPrices = mergePriceIntoSet(exPrices, newPrice);
            }

            //Removing intersecting prices with same value
            Map <Long, List<Price>> groupedByValue = exPrices.stream()
                    .collect(Collectors.groupingBy(Price::getValue));
            List<Price> toBeRemoved = new ArrayList<>();
            for (Long value : groupedByValue.keySet()) {
                List<Price> priceList = groupedByValue.get(value)
                        .stream()
                        .sorted(Comparator.comparing(Price::getBegin))
                        .collect(Collectors.toList());
                for (int i = 0; i < priceList.size() - 1; i++) {
                    Price currPrice = priceList.get(i);
                    Price nextPrice = priceList.get(i + 1);
                    if (currPrice.getEnd().after(nextPrice.getBegin())) {
                        toBeRemoved.add(nextPrice);
                        currPrice.setEnd(nextPrice.getEnd());
                    }
                }
            }
            toBeRemoved.forEach(exPrices::remove);

            mergedPrices.addAll(exPrices);
        }

        // Adding prices with unique keys
        existingKeys.removeAll(intersection);
        newKeys.removeAll(intersection);
        for (CompositeKey key : existingKeys) {
            mergedPrices.addAll(existingPricesMap.get(key));
        }
        for (CompositeKey key : newKeys) {
            mergedPrices.addAll(newPricesMap.get(key));
        }

        return mergedPrices;
    }


    /**
     * Merge new price into set of existing prices
     * Merges new prices with each element of set
     * @param prices - Set of existing prices
     * @param price  - price that needs to be merged
     * @return - set of merged prices
     */
    private static Set<Price> mergePriceIntoSet(Set<Price> prices, Price price) {
        boolean hasIntersection = false;
        Set<Price> result = new HashSet<>();
        for (Price existingPrice : prices) {
            if (existingPrice.getBegin().after(price.getEnd())
                    || existingPrice.getEnd().before(price.getBegin())) {
                // there is no intersection with existing price, so it adds to result
                result.add(existingPrice);
            } else {
                hasIntersection = true;
                result.addAll(mergeTwoPrices(existingPrice, price));
            }
        }
        if (!hasIntersection) {
            result.add(price);
        }
        return result;
    }


    /**
     * Merges two prices
     * Prices must intersect each other in some way
     * @param existingPrice - existing price
     * @param newPrice      - price needs to be merged
     * @return - set of merged prices, returns set, cause in some situations
     * there will be more than one prices in result
     */
    private static Set<Price> mergeTwoPrices(Price existingPrice, Price newPrice) {
        Set<Price> result = new HashSet<>();
        //New price inside existing price
        if (existingPrice.getBegin().before(newPrice.getBegin())
                && existingPrice.getEnd().after(newPrice.getEnd())) {
            if (existingPrice.getValue() == newPrice.getValue()) {
                result.add(existingPrice);
            } else {
                //Split existing price in two parts
                Price newExistingPrice = new Price(
                        existingPrice.getProductCode(),
                        existingPrice.getNumber(),
                        existingPrice.getDepart(),
                        existingPrice.getBegin(),
                        newPrice.getBegin(),
                        existingPrice.getValue());
                existingPrice.setBegin(newPrice.getEnd());
                result.add(newPrice);
                result.add(newExistingPrice);
                result.add(existingPrice);
            }
            return result;
        }
        //Existing price is inside new price
        if (existingPrice.getBegin().after(newPrice.getBegin())
                && existingPrice.getEnd().before(newPrice.getEnd())) {
            result.add(newPrice);
            return result;
        }
        //New price intersects existing one left side of existing one
        if (((existingPrice.getBegin().after(newPrice.getBegin()))
                && existingPrice.getBegin().before(newPrice.getEnd())
                && existingPrice.getEnd().after(newPrice.getEnd()))
                || existingPrice.getBegin().equals(newPrice.getEnd())) {
            if (existingPrice.getValue() == newPrice.getValue()) {
                existingPrice.setBegin(newPrice.getBegin());
                result.add(existingPrice);
            } else {
                existingPrice.setBegin(newPrice.getEnd());
                result.add(newPrice);
                result.add(existingPrice);
            }
            return result;
        }
        //New price intersects existing one right side of existing one
        if ((existingPrice.getEnd().after(newPrice.getBegin())
                && existingPrice.getEnd().before(newPrice.getEnd())
                && existingPrice.getBegin().before(newPrice.getBegin()))
                || existingPrice.getEnd().equals(newPrice.getBegin())) {
            if (existingPrice.getValue() == newPrice.getValue()) {
                existingPrice.setEnd(newPrice.getEnd());
                result.add(existingPrice);
            } else {
                existingPrice.setEnd(newPrice.getBegin());
                result.add(existingPrice);
                result.add(newPrice);
            }

        }
        return result;
    }

    /**
     * Grouping prices by CompositeKey (productionCode, number, depart)
     * @param prices - Collection of prices
     * @return map with CompositeKey as key and Set of prices as value
     */
    private static Map<CompositeKey, Set<Price>> getPricesMap(Collection<Price> prices) {
        Map<CompositeKey, Set<Price>> pricesMap = new HashMap<>();
        prices.forEach(x -> {
            CompositeKey compositeKey = new CompositeKey(x.getProductCode(), x.getNumber(), x.getDepart());
            if (pricesMap.containsKey(compositeKey)) {
                pricesMap.get(compositeKey).add(x);
            } else {
                pricesMap.put(compositeKey, new HashSet<>(Set.of(x)));
            }
        });
        return pricesMap;
    }


    private static class CompositeKey {
        private final String productCode;
        private final int number;
        private final int depart;

        public CompositeKey(String productCode, int number, int depart) {
            this.productCode = productCode;
            this.number = number;
            this.depart = depart;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CompositeKey that = (CompositeKey) o;
            return number == that.number && depart == that.depart && Objects.equals(productCode, that.productCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productCode, number, depart);
        }

        @Override
        public String toString() {
            return "CompositeKey{" +
                    "productCode='" + productCode + '\'' +
                    ", number=" + number +
                    ", depart=" + depart +
                    '}';
        }
    }
}