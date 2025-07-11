package site.ahzx.core.impl;

import site.ahzx.core.TaskExecutor;
import site.ahzx.domain.entity.TaskDef;

import java.util.Map;

public class ApiTaskExecutor implements TaskExecutor {
    public void execute(TaskDef task, Map<String, String> context) {
        System.out.println("[API] Calling " + task.getTaskId() + " with context " + context);
        // 实际 API 调用逻辑
    }
}
