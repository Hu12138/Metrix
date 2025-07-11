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
 * API 类型任务定义表，用于描述如何通过 API 拉取数据写入数据节点 实体类。
 *
 * @author xuefenghu
 * @since 2025-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("mtr_api_task")
public class ApiTask implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识该 API 任务的 ID
     */
    @Id
    private String id;

    /**
     * API 名称（供后端映射或路由调用）
     */
    private String apiName;

    /**
     * 默认 API 参数模板，支持动态 merge context
     */
    private String defaultParams;

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
