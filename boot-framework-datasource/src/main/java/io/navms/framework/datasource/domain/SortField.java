package io.navms.framework.datasource.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 排序字段
 *
 * @author navms
 */
@Data
public class SortField implements Serializable {

    @Serial
    private static final long serialVersionUID = -5060978050901187977L;

    /**
     * 排序字段
     */
    private String column;

    /**
     * 排序方式（asc、desc）
     */
    private boolean asc;

    public static SortField asc(String column) {
        return build(column, true);
    }

    public static SortField desc(String column) {
        return build(column, false);
    }

    private static SortField build(String column, boolean isAsc) {
        SortField sortField = new SortField();
        sortField.setColumn(column);
        sortField.setAsc(isAsc);
        return sortField;
    }

}
