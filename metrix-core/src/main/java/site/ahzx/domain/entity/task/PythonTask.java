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
 * Python 类型任务定义表，用于执行自定义脚本处理逻辑 实体类。
 *
 * @author xuefenghu
 * @since 2025-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("mtr_python_task")
public class PythonTask implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识该 Python 任务的 ID
     */
    @Id
    private String id;

    /**
     * Python 脚本路径（可为绝对路径或模块路径）
     */
    private String scriptPath;

    /**
     * 执行入口函数名
     */
    private String entryFunction;

    /**
     * 运行环境配置，如 Python 虚拟环境、依赖等
     */
    private String envConfig;

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
