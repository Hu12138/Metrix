package com.metrix.sqlgenerator.service;

import com.metrix.sqlgenerator.model.DataSource;
import com.metrix.sqlgenerator.model.Filter;
import com.metrix.sqlgenerator.model.TimeRange;
import java.util.List;

public interface DataFetcher {
    /**
     * 获取缺失数据
     * @param dataSource 数据源信息
     * @param basicFilters 基础过滤条件
     * @param missingRanges 需要补充的时间范围
     */
    void fetchMissingData(DataSource dataSource, List<Filter> basicFilters, List<TimeRange> missingRanges);
}
