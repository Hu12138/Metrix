package site.ahzx.core.impl;

import site.ahzx.core.TaskExecutor;
import site.ahzx.domain.entity.TaskDef;

import java.util.Map;

public class SqlTaskExecutor implements TaskExecutor {

    public void execute(TaskDef task, Map<String, String> context) {
        System.out.println("[SQL] Executing " + task.getTaskId() + " with context " + context);
        // 实际替换模板执行 SQL
    }
}
