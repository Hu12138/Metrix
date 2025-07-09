package com.metrix.sqlgenerator;

import com.metrix.sqlgenerator.model.DataSource;
import com.metrix.sqlgenerator.service.DataValidator;
import com.metrix.sqlgenerator.util.JsonUtils;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Executor {
    public static void main(String[] args) throws Exception {
        String jsonStr = """
                {
                  "metricList":[
                    {
                      "id": 1,
                      "dataSource": {
                        "database": "mock",
                        "table": "tax_registration"
                      },
                      "metricName": "total_nums_by_金融业",
                      "dimensions": ["hy"],
                      "basicFilters": [
                          {
                            "field": "hy",
                            "operator": "=",
                            "value": "金融业"
                          },
                          {
                            "field": "djrq",
                            "operator": "BETWEEN",
                            "value": ["2023-01-01", "2023-12-31"]
                          }
                        ],
                      "filters": [
                        {
                          "field": "djlx",
                          "operator": "=",
                          "value": 1
                        }
                      ],
                      "metric": {
                        "type": "count",
                        "field": "nsrmc",
                        "alias": "total_nums"
                      }
                    },
                    {
                      "id": 2,
                      "dataSource": {
                        "database": "sales_db",
                        "table": "orders2"
                      },
                      "metricName": "total_sales_by_region2",
                      "dimensions": ["region"],
                      "basicFilters": [
                          {
                            "field": "enterprise_id",
                            "operator": "=",
                            "value": "E12345"
                          },
                          {
                            "field": "date",
                            "operator": "BETWEEN",
                            "value": ["2023-01-01", "2023-12-31"]
                          }
                        ],
                      "filters": [
                        {
                          "field": "order_status",
                          "operator": "=",
                          "value": "completed"
                        }
                      ],
                      "metric": {
                        "type": "sum",
                        "field": "order_amount",
                        "alias": "total_sales"
                      }
                    }
                  ]
                }
                """;
        // 解析请求
        var queryRequest = JsonUtils.parseQuery(jsonStr);
        // 验证数据集
        DataValidator dataValidator = new DataValidator();
        Map<DataSource, DataValidator.DataCheckResult>  dataCheckResult = dataValidator.validateData(queryRequest);
//        log.info("data validation result: {}", dataCheckResult);
        dataCheckResult.forEach((result, dataSource) -> {
            log.info("Data validation result for {}: {}", dataSource, result);
        });
        // 生成SQL
        Map<String, String> sqlMap = SQLGenerator.generateSQL(queryRequest);
        
        log.info("Generated SQL Map:");
        sqlMap.forEach((metricName, sql) -> {
            log.info("{}:\n{}", metricName, sql);
        });
        
        System.out.println("Final SQLs:");
        sqlMap.forEach((metricName, sql) -> {
            System.out.println("=== " + metricName + " ===");
            System.out.println(sql);
            System.out.println();
        });
    }
}
