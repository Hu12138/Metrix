package site.ahzx.core;

import java.util.*;

public class DAG  {
    Map<String, DAGNode> nodes = new HashMap<>();
    void addNode(DAGNode node) {
        nodes.put(node.nodeId, node);
    }
    List<DAGNode> topologicalSort() throws Exception {
        List<DAGNode> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> visiting = new HashSet<>();

        for (DAGNode node : nodes.values()) {
            dfs(node.nodeId, visited, visiting, result);
        }
        Collections.reverse(result);
        return result;
    }
    private void dfs(String nodeId, Set<String> visited, Set<String> visiting, List<DAGNode> result) throws Exception {
        if (visited.contains(nodeId)) return;
        if (visiting.contains(nodeId)) throw new Exception("Cycle detected in DAG");

        visiting.add(nodeId);
        DAGNode node = nodes.get(nodeId);
        for (String depId : node.dependencies) {
            dfs(depId, visited, visiting, result);
        }
        visiting.remove(nodeId);
        visited.add(nodeId);
        result.add(node);
    }
}
