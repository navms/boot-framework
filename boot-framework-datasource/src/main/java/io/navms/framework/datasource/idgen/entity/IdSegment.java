package io.navms.framework.datasource.idgen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 号段
 *
 * @author navms
 */
@Data
@TableName("id_segment")
public class IdSegment {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 号段 Key
     */
    @TableField(value = "`key`")
    private String key;

    /**
     * 号段最大 ID 值
     */
    private Long maxId;

    /**
     * 步长
     */
    private Integer step;

    /**
     * 描述
     */
    private String description;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}

