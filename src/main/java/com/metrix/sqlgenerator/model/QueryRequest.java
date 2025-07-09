package com.metrix.sqlgenerator.model;

import lombok.Data;
import java.util.List;

@Data
public class QueryRequest {
    private List<MetricConfig> metricList;
    private TimeRange timeRange;

    @Data
    public static class MetricConfig {
        private int id;
        private DataSource dataSource;
        private String metricName;
        private List<String> dimensions;
        private List<Filter> basicFilters;
        private List<Filter> filters;
        private Metric metric;
    }
}
