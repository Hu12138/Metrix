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
 * 数据节点元信息表，用于描述所有数据表/API节点 实体类。
 *
 * @author xuefenghu
 * @since 2025-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("mtr_data_node")
public class DataNode implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识当前数据节点（如 ads_user_profile）
     */
    @Id
    private String nodeId;

    /**
     * 数据节点名称，通常用于展示
     */
    private String name;

    /**
     * 数据层级（如: ods / dwd / ads / indicator）
     */
    private String layer;

    /**
     * Doris 中实际存储的物理表名，可为空
     */
    private String physicalTable;

    /**
     * 该数据节点的业务含义说明
     */
    private String description;

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
