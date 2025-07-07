package com.metrix.sqlgenerator.model;

import lombok.Data;
import java.util.List;

@Data
public class QueryRequest {
    private DataSource dataSource;
    private String metricName;
    private List<String> dimensions;
    private List<Filter> filters;
    private Metric metric;
}
