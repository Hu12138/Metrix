package com.metrix.sqlgenerator;

import com.metrix.sqlgenerator.model.*;
import com.metrix.sqlgenerator.util.JsonUtils;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.LinkedHashMap;

public class SQLGenerator {
    public static Map<String, String> generateSQL(QueryRequest request) throws Exception {
        validateRequest(request);
        return buildSQL(request);
    }

    public static Map<String, String> generateSQL(String jsonInput) throws Exception {
        QueryRequest request = JsonUtils.parseQuery(jsonInput);
        return generateSQL(request);
    }

    private static void validateRequest(QueryRequest request) throws IllegalArgumentException {
        if (request.getMetricList() == null || request.getMetricList().isEmpty()) {
            throw new IllegalArgumentException("At least one metric config is required");
        }
        
        for (QueryRequest.MetricConfig metricConfig : request.getMetricList()) {
            if (metricConfig.getDataSource() == null || 
                metricConfig.getDataSource().getDatabase() == null || 
                metricConfig.getDataSource().getTable() == null) {
                throw new IllegalArgumentException("Missing required dataSource fields");
            }
            if (metricConfig.getMetric() == null || 
                metricConfig.getMetric().getType() == null || 
                metricConfig.getMetric().getField() == null) {
                throw new IllegalArgumentException("Missing required metric fields");
            }
        }
    }

    private static Map<String, String> buildSQL(QueryRequest request) {
        Map<String, String> sqlMap = new LinkedHashMap<>();
        
        for (QueryRequest.MetricConfig metricConfig : request.getMetricList()) {
            String select = buildSelectClause(metricConfig);
            String from = String.format("FROM %s.%s", 
                metricConfig.getDataSource().getDatabase(), 
                metricConfig.getDataSource().getTable());
            String where = buildWhereClause(metricConfig);
            String groupBy = buildGroupByClause(metricConfig);
            String sql = String.format("SELECT %s %s %s %s", 
                select, from, where, groupBy).trim();
            
            sqlMap.put(metricConfig.getMetricName(), sql);
        }
        
        return sqlMap;
    }



    private static String buildSelectClause(QueryRequest.MetricConfig metricConfig) {
        String dimensions = metricConfig.getDimensions() != null ? 
            metricConfig.getDimensions().stream()
                .collect(Collectors.joining(", ")) : "";
        
        String metric = String.format("%s(%s) AS %s", 
            metricConfig.getMetric().getType(), 
            metricConfig.getMetric().getField(), 
            metricConfig.getMetric().getAlias());
        
        return dimensions.isEmpty() ? metric : dimensions + ", " + metric;
    }

    private static String buildWhereClause(QueryRequest.MetricConfig metricConfig) {
        StringBuilder whereBuilder = new StringBuilder();
        
        // 处理基础过滤条件
        if (metricConfig.getBasicFilters() != null && !metricConfig.getBasicFilters().isEmpty()) {
            whereBuilder.append(metricConfig.getBasicFilters().stream()
                .map(f -> {
                    if (f.getOperator() == OperatorType.BETWEEN) {
                        try {
                            Object value = f.getValue();
                            String[] values;
                            
                            if (value instanceof String[]) {
                                values = (String[]) value;
                            } else if (value instanceof String) {
                                // 兼容旧格式：字符串用逗号分隔
                                values = ((String)value).split(",");
                                if (values.length != 2) {
                                    throw new IllegalArgumentException("BETWEEN requires exactly 2 values");
                                }
                            } else if (value instanceof java.util.List) {
                                // 处理JSON数组反序列化为List的情况
                                java.util.List<?> list = (java.util.List<?>) value;
                                values = list.stream()
                                    .map(Object::toString)
                                    .toArray(String[]::new);
                            } else {
                                throw new IllegalArgumentException("Unsupported value type for BETWEEN");
                            }
                            
                            if (values.length != 2) {
                                throw new IllegalArgumentException("BETWEEN requires exactly 2 values");
                            }
                            return String.format("%s BETWEEN '%s' AND '%s'",
                                f.getField(), values[0].trim(), values[1].trim());
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Invalid BETWEEN value format: " + e.getMessage());
                        }
                    } else {
                        return String.format("%s %s '%s'",
                            f.getField(),
                            f.getOperator().getSymbol(),
                            f.getValue());
                    }
                })
                .collect(Collectors.joining(" AND ")));
        }

        // 处理指标特有过滤条件
        if (metricConfig.getFilters() != null && !metricConfig.getFilters().isEmpty()) {
            if (whereBuilder.length() > 0) {
                whereBuilder.append(" AND ");
            }
            whereBuilder.append(metricConfig.getFilters().stream()
                .map(f -> String.format("%s %s '%s'", 
                    f.getField(), 
                    f.getOperator().getSymbol(), 
                    f.getValue()))
                .collect(Collectors.joining(" AND ")));
        }

        return whereBuilder.length() > 0 ? "WHERE " + whereBuilder.toString() : "";
    }

    private static String buildGroupByClause(QueryRequest.MetricConfig metricConfig) {
        if (metricConfig.getDimensions() == null || metricConfig.getDimensions().isEmpty()) {
            return "";
        }
        return "GROUP BY " + String.join(", ", metricConfig.getDimensions());
    }
}
