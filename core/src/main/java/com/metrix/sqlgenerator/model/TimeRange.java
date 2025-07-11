package com.metrix.sqlgenerator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeRange {
    // Getters and Setters
    private LocalDate startDate;
    private LocalDate endDate;


}
