package com.metrix.sqlgenerator.service;

import com.metrix.sqlgenerator.model.QueryRequest;
import com.metrix.sqlgenerator.model.TimeRange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataValidator {
    private static final String JDBC_URL = "jdbc:mysql://192.168.8.203:3306/test_db";
    private static final String USER = "test";
    private static final String PASSWORD = "123456";

    public DataCheckResult validateData(QueryRequest request) {
        DataCheckResult result = new DataCheckResult();
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            // 1. 验证表是否存在
            if (!validateTableExists(conn, request.getDataSource().getTable())) {
                result.setDataComplete(false);
                return result;
            }

            // 2. 验证字段存在性
            if (!validateFieldsExist(conn, request)) {
                result.setDataComplete(false);
                return result;
            }

            // 3. 检查时间范围内数据连续性
            if (request.getTimeRange() != null) {
                List<TimeRange> missingRanges = checkDataContinuity(conn, request);
                if (!missingRanges.isEmpty()) {
                    result.setDataComplete(false);
                    result.setMissingRanges(missingRanges);
                }
            }

            result.setDataComplete(true);
        } catch (SQLException e) {
            result.setDataComplete(false);
        }
        
        return result;
    }

    private boolean validateTableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getTables(null, null, tableName, null)) {
            return rs.next();
        }
    }

    private boolean validateFieldsExist(Connection conn, QueryRequest request) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        String tableName = request.getDataSource().getTable();
        
        // 检查指标字段
        if (!columnExists(meta, tableName, request.getMetric().getField())) {
            return false;
        }

        // 检查维度字段
        if (request.getDimensions() != null) {
            for (String dim : request.getDimensions()) {
                if (!columnExists(meta, tableName, dim)) {
                    return false;
                }
            }
        }

        // 检查过滤条件字段
        if (request.getFilters() != null) {
            for (var filter : request.getFilters()) {
                if (!columnExists(meta, tableName, filter.getField())) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean columnExists(DatabaseMetaData meta, String tableName, String columnName) throws SQLException {
        try (ResultSet rs = meta.getColumns(null, null, tableName, columnName)) {
            return rs.next();
        }
    }

    private List<TimeRange> checkDataContinuity(Connection conn, QueryRequest request) throws SQLException {
        List<TimeRange> missingRanges = new ArrayList<>();
        String sql = String.format(
            "SELECT MIN(timestamp) as start, MAX(timestamp) as end FROM %s WHERE timestamp BETWEEN ? AND ?",
            request.getDataSource().getTable()
        );

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(request.getTimeRange().getStartDate().atStartOfDay()));
            stmt.setTimestamp(2, Timestamp.valueOf(request.getTimeRange().getEndDate().atStartOfDay()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp actualStart = rs.getTimestamp("start");
                    Timestamp actualEnd = rs.getTimestamp("end");
                    
                    // 检查数据是否完整
                    if (actualStart == null || actualEnd == null) {
                        missingRanges.add(new TimeRange(
                            request.getTimeRange().getStartDate(),
                            request.getTimeRange().getEndDate()
                        ));
                    } else {
                        if (actualStart.after(Timestamp.valueOf(request.getTimeRange().getStartDate().atStartOfDay()))) {
                            missingRanges.add(new TimeRange(
                                request.getTimeRange().getStartDate(),
                                actualStart.toLocalDateTime().toLocalDate()
                            ));
                        }
                        if (actualEnd.before(Timestamp.valueOf(request.getTimeRange().getEndDate().atStartOfDay()))) {
                            missingRanges.add(new TimeRange(
                                actualEnd.toLocalDateTime().toLocalDate(),
                                request.getTimeRange().getEndDate()
                            ));
                        }
                    }
                }
            }
        }
        
        return missingRanges;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataCheckResult {
        private boolean dataComplete;
        private List<TimeRange> missingRanges;
    }
}
