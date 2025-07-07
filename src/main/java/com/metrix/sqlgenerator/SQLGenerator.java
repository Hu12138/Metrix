package com.metrix.sqlgenerator;

import com.metrix.sqlgenerator.model.*;
import com.metrix.sqlgenerator.util.JsonUtils;
import java.util.stream.Collectors;

public class SQLGenerator {
    public static String generateSQL(QueryRequest request) throws Exception {
        validateRequest(request);
        return buildSQL(request);
    }

    public static String generateSQL(String jsonInput) throws Exception {
        QueryRequest request = JsonUtils.parseQuery(jsonInput);
        return generateSQL(request);
    }

    private static void validateRequest(QueryRequest request) throws IllegalArgumentException {
        if (request.getDataSource() == null || 
            request.getDataSource().getDatabase() == null || 
            request.getDataSource().getTable() == null) {
            throw new IllegalArgumentException("Missing required dataSource fields");
        }
        if (request.getMetric() == null || 
            request.getMetric().getType() == null || 
            request.getMetric().getField() == null) {
            throw new IllegalArgumentException("Missing required metric fields");
        }
    }

    private static String buildSQL(QueryRequest request) {
        String select = buildSelectClause(request);
        String from = String.format("FROM %s.%s", 
            request.getDataSource().getDatabase(), 
            request.getDataSource().getTable());
        String where = buildWhereClause(request);
        String groupBy = buildGroupByClause(request);

        return String.format("SELECT %s %s %s %s", 
            select, from, where, groupBy).trim();
    }

    private static String buildSelectClause(QueryRequest request) {
        String dimensions = request.getDimensions() != null ? 
            request.getDimensions().stream()
                .collect(Collectors.joining(", ")) : "";
        
        String metric = String.format("%s(%s) AS %s", 
            request.getMetric().getType(), 
            request.getMetric().getField(), 
            request.getMetric().getAlias());
        
        return dimensions.isEmpty() ? metric : dimensions + ", " + metric;
    }

    private static String buildWhereClause(QueryRequest request) {
        if (request.getFilters() == null || request.getFilters().isEmpty()) {
            return "";
        }
        
        String conditions = request.getFilters().stream()
            .map(f -> String.format("%s %s '%s'", 
                f.getField(), 
                f.getOperator().getSymbol(), 
                f.getValue()))
            .collect(Collectors.joining(" AND "));
        
        return "WHERE " + conditions;
    }

    private static String buildGroupByClause(QueryRequest request) {
        if (request.getDimensions() == null || request.getDimensions().isEmpty()) {
            return "";
        }
        return "GROUP BY " + String.join(", ", request.getDimensions());
    }
}
