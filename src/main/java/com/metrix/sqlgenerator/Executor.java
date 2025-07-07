package com.metrix.sqlgenerator;

import com.metrix.sqlgenerator.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Executor {
    public static void main(String[] args) throws Exception {
        String jsonStr = """
                {
                      "dataSource": {
                        "database": "sales_db",
                        "table": "orders"
                      },
                      "metricName": "total_sales_by_region",
                      "dimensions": ["region"],
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
                """;
        var queryRequest = JsonUtils.parseQuery(jsonStr);
        String sql = SQLGenerator.generateSQL(queryRequest);
        
        log.info("Generated SQL:\n{}", sql);
        System.out.println("Final SQL:");
        System.out.println(sql);
    }
}
