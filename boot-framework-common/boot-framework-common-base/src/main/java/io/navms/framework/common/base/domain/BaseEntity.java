package io.navms.framework.common.base.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 基础实体类
 *
 * @author navms
 */
@Data
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6616707427930664146L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 扩展
     */
    private Map<String, Object> extend;

    public void putExtend(String key, Object value) {
        extend = Optional.ofNullable(extend).orElse(new HashMap<>());
        extend.put(key, value);
    }

    public Object getExtend(String key) {
        return Optional.ofNullable(extend).map(map -> map.get(key)).orElse(null);
    }

}
