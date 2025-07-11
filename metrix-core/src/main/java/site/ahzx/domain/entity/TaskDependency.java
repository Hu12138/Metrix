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
 * 任务依赖关系表，用于记录每个任务所依赖的数据节点（即血缘关系） 实体类。
 *
 * @author xuefenghu
 * @since 2025-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("mtr_task_dependency")
public class TaskDependency implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前任务 ID（指向 task_def.task_id）
     */
    @Id
    private String taskId;

    /**
     * 该任务依赖的上游数据节点（data_node.node_id）
     */
    @Id
    private String dependsOnNodeId;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 修改人
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
