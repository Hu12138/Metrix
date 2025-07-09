package com.metrix.sqlgenerator.service;

import com.metrix.sqlgenerator.model.DataSource;
import com.metrix.sqlgenerator.model.Filter;
import com.metrix.sqlgenerator.model.QueryRequest;
import com.metrix.sqlgenerator.model.TimeRange;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DataValidator {
    private static final String JDBC_URL = "jdbc:mysql://192.168.8.203:3306/test_db";
    private static final String USER = "test";
    private static final String PASSWORD = "123456";

    public DataCheckResult validateData(QueryRequest request) {
        DataCheckResult result = new DataCheckResult();
        Set<DataSource> checkedDataSources = new HashSet<>();
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            // 合并验证流程 - 单次遍历所有指标配置
            for (QueryRequest.MetricConfig metricConfig : request.getMetricList()) {
                DataSource dataSource = metricConfig.getDataSource();
                String tableName = dataSource.getTable();
                DatabaseMetaData meta = conn.getMetaData();
                
                // 去重检查数据源
                if (!checkedDataSources.contains(dataSource)) {
                    // 验证表是否存在
                    if (!validateTableExists(conn, tableName)) {
                        result.setValidationCode(DataCheckResult.ValidationCode.TABLE_NOT_FOUND);
                        result.setValidationMessage("表 " + tableName + " 不存在");
                        result.setDataComplete(false);
                        return result;
                    }
                    checkedDataSources.add(dataSource);
                    
                    // 验证基础过滤条件字段
                    if (metricConfig.getBasicFilters() != null) {
                        for (Filter filter : metricConfig.getBasicFilters()) {
                            if (!columnExists(meta, tableName, filter.getField())) {
                                result.setValidationCode(DataCheckResult.ValidationCode.COLUMN_NOT_FOUND);
                                result.setValidationMessage("字段 " + filter.getField() + " 不存在");
                                result.setDataComplete(false);
                                return result;
                            }
                        }
                    }
                }

                // 验证指标字段
                if (!columnExists(meta, tableName, metricConfig.getMetric().getField())) {
                    result.setValidationCode(DataCheckResult.ValidationCode.COLUMN_NOT_FOUND);
                    result.setValidationMessage("指标字段 " + metricConfig.getMetric().getField() + " 不存在");
                    result.setDataComplete(false);
                    return result;
                }

                // 验证维度字段
                if (metricConfig.getDimensions() != null) {
                    for (String dim : metricConfig.getDimensions()) {
                        if (!columnExists(meta, tableName, dim)) {
                            result.setValidationCode(DataCheckResult.ValidationCode.COLUMN_NOT_FOUND);
                            result.setValidationMessage("维度字段 " + dim + " 不存在");
                            result.setDataComplete(false);
                            return result;
                        }
                    }
                }

                // 验证过滤条件字段
                if (metricConfig.getFilters() != null) {
                    for (Filter filter : metricConfig.getFilters()) {
                        if (!columnExists(meta, tableName, filter.getField())) {
                            result.setValidationCode(DataCheckResult.ValidationCode.COLUMN_NOT_FOUND);
                            result.setValidationMessage("过滤条件字段 " + filter.getField() + " 不存在");
                            result.setDataComplete(false);
                            return result;
                        }
                    }
                }
            }

            // 检查时间范围内数据连续性
            if (request.getTimeRange() != null) {
                List<TimeRange> missingRanges = new ArrayList<>();
                for (DataSource dataSource : checkedDataSources) {
                    missingRanges.addAll(checkDataContinuity(conn, dataSource, request.getTimeRange()));
                }
                if (!missingRanges.isEmpty()) {
                    result.setValidationCode(DataCheckResult.ValidationCode.DATA_INCOMPLETE);
                    result.setValidationMessage("数据不完整，存在缺失时间段");
                    result.setDataComplete(false);
                    result.setMissingRanges(missingRanges);
                    return result;
                }
            }

            result.setValidationCode(DataCheckResult.ValidationCode.SUCCESS);
            result.setValidationMessage("验证通过");
            result.setDataComplete(true);
        } catch (SQLException e) {
            result.setValidationCode(DataCheckResult.ValidationCode.CONNECTION_FAILED);
            result.setValidationMessage("数据库连接失败: " + e.getMessage());
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

    private boolean validateFieldsExist(Connection conn, QueryRequest.MetricConfig metricConfig) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        String tableName = metricConfig.getDataSource().getTable();
        
        // 检查指标字段
        if (!columnExists(meta, tableName, metricConfig.getMetric().getField())) {
            return false;
        }

        // 检查维度字段
        if (metricConfig.getDimensions() != null) {
            for (String dim : metricConfig.getDimensions()) {
                if (!columnExists(meta, tableName, dim)) {
                    return false;
                }
            }
        }

        // 检查过滤条件字段
        if (metricConfig.getFilters() != null) {
            for (var filter : metricConfig.getFilters()) {
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

    private List<TimeRange> checkDataContinuity(Connection conn, DataSource dataSource, TimeRange timeRange) throws SQLException {
        List<TimeRange> missingRanges = new ArrayList<>();
        String sql = String.format(
            "SELECT MIN(timestamp) as start, MAX(timestamp) as end FROM %s WHERE timestamp BETWEEN ? AND ?",
            dataSource.getTable()
        );

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(timeRange.getStartDate().atStartOfDay()));
            stmt.setTimestamp(2, Timestamp.valueOf(timeRange.getEndDate().atStartOfDay()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp actualStart = rs.getTimestamp("start");
                    Timestamp actualEnd = rs.getTimestamp("end");
                    
                    // 检查数据是否完整
                    if (actualStart == null || actualEnd == null) {
                        missingRanges.add(new TimeRange(
                            timeRange.getStartDate(),
                            timeRange.getEndDate()
                        ));
                    } else {
                        if (actualStart.after(Timestamp.valueOf(timeRange.getStartDate().atStartOfDay()))) {
                            missingRanges.add(new TimeRange(
                                timeRange.getStartDate(),
                                actualStart.toLocalDateTime().toLocalDate()
                            ));
                        }
                        if (actualEnd.before(Timestamp.valueOf(timeRange.getEndDate().atStartOfDay()))) {
                            missingRanges.add(new TimeRange(
                                actualEnd.toLocalDateTime().toLocalDate(),
                                timeRange.getEndDate()
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
        private ValidationCode validationCode;
        private String validationMessage;

        public enum ValidationCode {
            SUCCESS(0, "验证通过"),
            TABLE_NOT_FOUND(1001, "表不存在"),
            COLUMN_NOT_FOUND(1002, "字段不存在"), 
            DATA_INCOMPLETE(1003, "数据不完整"),
            CONNECTION_FAILED(1004, "数据库连接失败");

            private final int code;
            private final String message;

            ValidationCode(int code, String message) {
                this.code = code;
                this.message = message;
            }

            public int getCode() {
                return code;
            }

            public String getMessage() {
                return message;
            }
        }
    }
}
