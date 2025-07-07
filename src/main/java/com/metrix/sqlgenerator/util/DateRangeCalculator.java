package com.metrix.sqlgenerator.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateRangeCalculator {
    public static String[] calculateRange(String rangeType, int value) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch(rangeType.toLowerCase()) {
            case "last_n_years" -> endDate.minusYears(value);
            case "last_n_months" -> endDate.minusMonths(value);
            case "ytd" -> LocalDate.of(endDate.getYear(), 1, 1);
            default -> throw new IllegalArgumentException("Unsupported rangeType: " + rangeType);
        };
        return new String[]{
            startDate.format(DateTimeFormatter.ISO_DATE),
            endDate.format(DateTimeFormatter.ISO_DATE)
        };
    }
}
