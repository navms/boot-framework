package io.navms.framework.datasource.extension;

import io.navms.framework.common.base.utils.ArrayUtils;
import io.navms.framework.common.base.utils.CollectionUtils;
import io.navms.framework.common.base.utils.StringUtils;

import java.io.Serial;
import java.util.Collection;

/**
 * 拓展 MyBatis Plus QueryWrapper 类，主要增加如下功能：
 * <p>
 * 1. 拼接条件的方法，增加 xxxIfPresent 方法，用于判断值不存在的时候，不要拼接到条件中。
 *
 * @author navms
 */
public class QueryWrapper<T> extends com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<T> {

    @Serial
    private static final long serialVersionUID = -7062148941887850108L;

    public QueryWrapper<T> likeIfPresent(String column, String val) {
        if (StringUtils.isNotEmpty(val)) {
            return (QueryWrapper<T>) super.like(column, val);
        }
        return this;
    }

    public QueryWrapper<T> inIfPresent(String column, Collection<?> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            return (QueryWrapper<T>) super.in(column, values);
        }
        return this;
    }

    public QueryWrapper<T> inIfPresent(String column, Object... values) {
        if (ArrayUtils.isNotEmpty(values)) {
            return (QueryWrapper<T>) super.in(column, values);
        }
        return this;
    }

    public QueryWrapper<T> eqIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapper<T>) super.eq(column, val);
        }
        return this;
    }

    public QueryWrapper<T> neIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapper<T>) super.ne(column, val);
        }
        return this;
    }

    public QueryWrapper<T> gtIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapper<T>) super.gt(column, val);
        }
        return this;
    }

    public QueryWrapper<T> geIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapper<T>) super.ge(column, val);
        }
        return this;
    }

    public QueryWrapper<T> ltIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapper<T>) super.lt(column, val);
        }
        return this;
    }

    public QueryWrapper<T> leIfPresent(String column, Object val) {
        if (val != null) {
            return (QueryWrapper<T>) super.le(column, val);
        }
        return this;
    }

    public QueryWrapper<T> betweenIfPresent(String column, Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            return (QueryWrapper<T>) super.between(column, val1, val2);
        }
        if (val1 != null) {
            return (QueryWrapper<T>) ge(column, val1);
        }
        if (val2 != null) {
            return (QueryWrapper<T>) le(column, val2);
        }
        return this;
    }

    public QueryWrapper<T> betweenIfPresent(String column, Object[] values) {
        if (values != null && values.length != 0 && values[0] != null && values[1] != null) {
            return (QueryWrapper<T>) super.between(column, values[0], values[1]);
        }
        if (values != null && values.length != 0 && values[0] != null) {
            return (QueryWrapper<T>) ge(column, values[0]);
        }
        if (values != null && values.length != 0 && values[1] != null) {
            return (QueryWrapper<T>) le(column, values[1]);
        }
        return this;
    }

    // ========== 重写父类方法，方便链式调用 ==========

    @Override
    public QueryWrapper<T> eq(boolean condition, String column, Object val) {
        super.eq(condition, column, val);
        return this;
    }

    @Override
    public QueryWrapper<T> eq(String column, Object val) {
        super.eq(column, val);
        return this;
    }

    @Override
    public QueryWrapper<T> orderByDesc(String column) {
        super.orderByDesc(true, column);
        return this;
    }

    @Override
    public QueryWrapper<T> last(String lastSql) {
        super.last(lastSql);
        return this;
    }

    @Override
    public QueryWrapper<T> in(String column, Collection<?> coll) {
        super.in(column, coll);
        return this;
    }

}
