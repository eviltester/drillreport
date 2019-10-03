package uk.co.compendiumdev.drillreporting.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StringIntegerHashMapSorter {
    private final Map<String, Integer> hashmap;

    public StringIntegerHashMapSorter(final Map<String, Integer> hashmap) {
        this.hashmap = hashmap;
    }

    public Map<String, Integer> sort() {
        return sortStringIntegerHashByValue(this.hashmap);
    }

    private Map<String, Integer> sortStringIntegerHashByValue(final Map<String, Integer> stringIntegerHash) {
        return stringIntegerHash.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
