package site.ahzx.core;

import site.ahzx.domain.entity.TaskDef;

import java.util.ArrayList;
import java.util.List;

public class MetadataStore {
    public static TaskDef getTaskByTargetNode(String nodeId) {
        // 模拟查询数据库
        return new TaskDef();
    }

    public static List<String> getDependencies(String taskId) {
        // 模拟依赖
        return new ArrayList<>();
    }
}
