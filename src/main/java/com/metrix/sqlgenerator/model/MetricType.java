package com.metrix.sqlgenerator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MetricType {
    AVG("avg"),
    SUM("sum"), 
    MAX("max"),
    COUNT("count"),
    MIN("min");

    private final String jsonValue;

    MetricType(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @JsonCreator
    public static MetricType fromString(String value) {
        for (MetricType type : MetricType.values()) {
            if (type.jsonValue.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown metric type: " + value);
    }
}
