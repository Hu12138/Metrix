package site.ahzx.core;

import java.util.HashMap;
import java.util.Map;

public class DAGMain {
    public static void main(String[] args) throws Exception {
        String targetNode = "indicator_risk_score";
        Map<String, String> context = new HashMap<>();
        context.put("enterprise_id", "E12345");
        context.put("request_id", "REQ_20250709_001");

        DAG dag = DAGBuilder.buildDAG(targetNode);
        DAGExecutor.executeDAG(dag, context);
    }
}
