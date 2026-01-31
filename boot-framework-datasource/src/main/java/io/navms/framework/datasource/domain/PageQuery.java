package io.navms.framework.datasource.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页查询参数
 *
 * @author navms
 */
@Data
public class PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1934753791707810312L;

    /**
     * 当前页码
     */
    private Integer current = 1;

    /**
     * 每页记录数
     */
    private Integer size = 10;

    /**
     * 排序字段
     */
    private List<SortField> orders;

}

