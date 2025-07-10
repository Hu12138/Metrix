package com.metrix.sqlgenerator;

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
                            "value": ["2003-01-01", "2023-12-31"]
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
                        "database": "mock",
                        "table": "small_business_profit"
                      },
                      "metricName": "max_yylr_by_nsrsbh",
                      "dimensions": ["nsrsbh"],
                      "basicFilters": [
                          {
                            "field": "nsrsbh",
                            "operator": "=",
                            "value": "91335378"
                          },
                          {
                            "field": "bbq",
                            "operator": "BETWEEN",
                            "value": ["2017-02", "2023-12"]
                          }
                        ],
                      "filters": [
                      ],
                      "metric": {
                        "type": "max",
                        "field": "yylr",
                        "alias": "max_yylr"
                      }
                    }
                  ]
                }
                """;
        // 解析请求
        var queryRequest = JsonUtils.parseQuery(jsonStr);

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
