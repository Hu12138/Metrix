package com.metrix.sqlgenerator.service;

import com.metrix.sqlgenerator.model.QueryRequest;
import com.metrix.sqlgenerator.model.TimeRange;
import java.util.List;

public interface DataFetcher {
    /**
     * 获取缺失数据
     * @param request 原始查询请求
     * @param missingRanges 需要补充的时间范围
     */
    void fetchMissingData(QueryRequest request, List<TimeRange> missingRanges);
}
