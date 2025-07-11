package site.ahzx.core;

import site.ahzx.core.impl.ApiTaskExecutor;
import site.ahzx.core.impl.SqlTaskExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAGExecutor {
    static Map<String, TaskExecutor> executors = new HashMap<>() {{
        put("sql", new SqlTaskExecutor());
        put("api", new ApiTaskExecutor());
    }};

    public static void executeDAG(DAG dag, Map<String, String> context) throws Exception {
        List<DAGNode> sorted = dag.topologicalSort();
        for (DAGNode node : sorted) {
            TaskExecutor executor = executors.get(node.task.getTaskType());
            if (executor == null) throw new RuntimeException("No executor for type: " + node.task.getTaskType());
            executor.execute(node.task, context);
        }
    }
}
