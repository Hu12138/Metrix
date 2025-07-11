package site.ahzx.domain.entity.task;

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
 * SQL 类型任务定义表，用于描述如何通过 SQL 生成某数据节点 实体类。
 *
 * @author xuefenghu
 * @since 2025-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("mtr_sql_task")
public class SqlTask implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识该 SQL 任务的 ID
     */
    @Id
    private String id;

    /**
     * SQL 模板，支持变量（如 :request_id / :enterprise_id）
     */
    private String sqlTemplate;

    /**
     * 执行引擎类型（如: doris / hive / clickhouse）
     */
    private String engine;

    /**
     * 补充说明 / 描述
     */
    private String notes;

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
