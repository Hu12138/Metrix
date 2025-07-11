package site.ahzx.core;

import site.ahzx.domain.entity.TaskDef;

import java.util.Map;

public interface TaskExecutor {
    public void execute(TaskDef task, Map<String, String> context);
}
