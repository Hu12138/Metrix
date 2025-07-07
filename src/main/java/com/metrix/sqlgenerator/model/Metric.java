package com.metrix.sqlgenerator.model;

import lombok.Data;

@Data
public class Metric {
    private MetricType type;
    private String field;
    private String alias;
}
