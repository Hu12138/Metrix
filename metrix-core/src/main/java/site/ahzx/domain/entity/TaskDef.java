package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务定义主表，统一管理任务调度入口，具体执行逻辑分表管理 实体类。
 *
 * @author xuefenghu
 * @since 2025-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("mtr_task_def")
public class TaskDef implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识任务（逻辑任务 ID）
     */
    @Id
    private String taskId;

    /**
     * 任务类型（如: sql / api / python / java）
     */
    private String taskType;

    /**
     * 任务实现的 ID，指向具体实现表（如 sql_task.id）
     */
    private String implId;

    /**
     * 任务的目标数据节点，即它要生成的表
     */
    private String targetNodeId;

    /**
     * 创建人用户名
     */
    private String createdBy;

    /**
     * 最后修改人用户名
     */
    private String updatedBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 最后修改时间
     */
    private LocalDateTime updatedAt;

}
