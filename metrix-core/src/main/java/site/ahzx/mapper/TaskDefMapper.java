package site.ahzx.mapper;

import com.mybatisflex.core.BaseMapper;
import site.ahzx.domain.entity.TaskDef;

/**
 * 任务定义主表，统一管理任务调度入口，具体执行逻辑分表管理 映射层。
 *
 * @author xuefenghu
 * @since 2025-07-11
 */
public interface TaskDefMapper extends BaseMapper<TaskDef> {

}
