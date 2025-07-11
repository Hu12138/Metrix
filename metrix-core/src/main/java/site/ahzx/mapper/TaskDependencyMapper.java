package site.ahzx.mapper;

import com.mybatisflex.core.BaseMapper;
import site.ahzx.domain.entity.TaskDependency;

/**
 * 任务依赖关系表，用于记录每个任务所依赖的数据节点（即血缘关系） 映射层。
 *
 * @author xuefenghu
 * @since 2025-07-11
 */
public interface TaskDependencyMapper extends BaseMapper<TaskDependency> {

}
