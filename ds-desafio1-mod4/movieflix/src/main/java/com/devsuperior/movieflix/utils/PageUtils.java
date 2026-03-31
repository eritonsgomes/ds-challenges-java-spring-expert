package com.devsuperior.movieflix.utils;

import org.springframework.data.domain.Sort;

import java.util.Arrays;

public class PageUtils {

    private PageUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String[] extractSortPropertyNames(String sortBy) {
        boolean hasComma = sortBy.contains(",");

        String[] propertyNames = { sortBy };

        if (hasComma) {
            propertyNames = sortBy.split(",");
        }

        boolean hasDirection = (sortBy.contains("ASC") || sortBy.contains("DESC"));

        if (hasDirection) {
            propertyNames = Arrays.stream(propertyNames)
                    .filter(p -> !(p.equals("ASC") || p.equals("DESC")))
                    .toArray(String[]::new);
        }

        return propertyNames;
    }

    public static Sort.Direction extractSortDirection(String sortBy) {
        Sort.Direction sortDirection = Sort.Direction.ASC;

        boolean hasDirection = (sortBy.contains("ASC") || sortBy.contains("DESC"));

        if (hasDirection) {
            String[] directions = sortBy.split(",");
            String direction = directions.length > 0 ? directions[directions.length - 1] : "";

            if (direction.equals("ASC") || direction.equals("DESC")) {
                sortDirection = Sort.Direction.valueOf(direction);
            }
        }

        return sortDirection;
    }

}
