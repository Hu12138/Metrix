# JSON转Doris SQL接口设计

## 接口基本信息
- 路径：POST /api/query
- Content-Type: application/json

## 请求参数
```json
{
  "dataSource": {
    "database": "string",
    "table": "string"
  },
  "metricName": "string",
  "dimensions": ["string"],
  "filters": [
    {
      "field": "string",
      "operator": "="|"!="|">"|"<"|">="|"<="|"like",
      "value": "string|number"
    }
  ],
  "metric": {
    "type": "sum|count|avg|max|min",
    "field": "string",
    "alias": "string"
  }
}
```

## 响应格式
成功：
```json
{
  "sql": "string",
  "status": "success"
}
```

失败：
```json
{
  "error": "string",
  "status": "error"
}
```

## SQL生成规则
1. SELECT部分：
   - 维度字段：直接列出
   - 指标字段：根据metric.type生成聚合函数

2. FROM部分：`database.table`

3. WHERE部分：
   - 多个filter条件用AND连接
   - 根据operator生成对应条件表达式

4. GROUP BY部分：所有维度字段

示例转换：
```sql
SELECT 
  region,
  SUM(order_amount) AS total_sales
FROM sales_db.orders
WHERE order_status = 'completed'
GROUP BY region
```

## 示例
请求：
```json
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
```

响应：
```json
{
  "sql": "SELECT region, SUM(order_amount) AS total_sales FROM sales_db.orders WHERE order_status = 'completed' GROUP BY region",
  "status": "success"
}
