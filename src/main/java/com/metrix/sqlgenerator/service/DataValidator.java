package com.metrix.sqlgenerator.service;

import com.metrix.sqlgenerator.model.QueryRequest;
import com.metrix.sqlgenerator.model.TimeRange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class DataValidator {
    public DataCheckResult validateData(QueryRequest request) {
        DataCheckResult result = new DataCheckResult();
        // TODO: 实现基础数据检查逻辑
        return result;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataCheckResult {
        private boolean dataComplete;
        private List<TimeRange> missingRanges;
        
        }
}
