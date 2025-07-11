package site.ahzx.core;

import site.ahzx.domain.entity.TaskDef;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DAGBuilder {
    public static DAG buildDAG(String targetNodeId) {
        DAG dag = new DAG();
        Set<String> visited = new HashSet<>();
        buildRecursive(targetNodeId, dag, visited);
        return dag;
    }

    private static void buildRecursive(String nodeId, DAG dag, Set<String> visited) {
        if (visited.contains(nodeId)) return;
        visited.add(nodeId);

        TaskDef task = MetadataStore.getTaskByTargetNode(nodeId);
        DAGNode node = new DAGNode();
        node.nodeId = nodeId;
        node.task = task;

        List<String> deps = MetadataStore.getDependencies(task.getTaskId());
        node.dependencies.addAll(deps);

        for (String dep : deps) {
            buildRecursive(dep, dag, visited);
        }

        dag.addNode(node);
    }
}
