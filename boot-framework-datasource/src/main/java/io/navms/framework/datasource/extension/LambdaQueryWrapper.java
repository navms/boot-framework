package io.navms.framework.datasource.extension;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.navms.framework.common.base.utils.ArrayUtils;
import io.navms.framework.common.base.utils.CollectionUtils;
import io.navms.framework.common.base.utils.StringUtils;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

/**
 * 增强 MyBatis Plus QueryWrapper 类，主要增加如下功能：
 * <p>
 * 1. 拼接条件的方法，增加 xxxIfPresent 方法，用于判断值不存在的时候，不要拼接到条件中。
 *
 * @author navms
 */
public class LambdaQueryWrapper<T> extends com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<T> {

    @Serial
    private static final long serialVersionUID = 1639923049771735623L;

    public LambdaQueryWrapper<T> likeIfPresent(SFunction<T, ?> column, String val) {
        if (StringUtils.isNotEmpty(val)) {
            return (LambdaQueryWrapper<T>) super.like(column, val);
        }
        return this;
    }

    public LambdaQueryWrapper<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            return (LambdaQueryWrapper<T>) super.in(column, values);
        }
        return this;
    }

    public LambdaQueryWrapper<T> inIfPresent(SFunction<T, ?> column, Object... values) {
        if (ArrayUtils.isNotEmpty(values)) {
            return (LambdaQueryWrapper<T>) super.in(column, values);
        }
        return this;
    }

    public LambdaQueryWrapper<T> eqIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (LambdaQueryWrapper<T>) super.eq(column, val);
        }
        return this;
    }

    public LambdaQueryWrapper<T> neIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (LambdaQueryWrapper<T>) super.ne(column, val);
        }
        return this;
    }

    public LambdaQueryWrapper<T> gtIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (LambdaQueryWrapper<T>) super.gt(column, val);
        }
        return this;
    }

    public LambdaQueryWrapper<T> geIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (LambdaQueryWrapper<T>) super.ge(column, val);
        }
        return this;
    }

    public LambdaQueryWrapper<T> ltIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (LambdaQueryWrapper<T>) super.lt(column, val);
        }
        return this;
    }

    public LambdaQueryWrapper<T> leIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (LambdaQueryWrapper<T>) super.le(column, val);
        }
        return this;
    }

    public LambdaQueryWrapper<T> betweenIfPresent(SFunction<T, ?> column, Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            return (LambdaQueryWrapper<T>) super.between(column, val1, val2);
        }
        if (val1 != null) {
            return (LambdaQueryWrapper<T>) ge(column, val1);
        }
        if (val2 != null) {
            return (LambdaQueryWrapper<T>) le(column, val2);
        }
        return this;
    }

    public LambdaQueryWrapper<T> betweenIfPresent(SFunction<T, ?> column, Object[] values) {
        return betweenIfPresent(column, ArrayUtils.get(values, 0), ArrayUtils.get(values, 1));
    }

    public LambdaQueryWrapper<T> betweenIfPresent(SFunction<T, ?> column, List<?> values) {
        if (CollectionUtils.isEmpty(values)) {
            return this;
        }
        if (CollectionUtils.size(values) == 1) {
            return betweenIfPresent(column, values.getFirst(), null);
        }
        return betweenIfPresent(column, CollectionUtils.get(values, 0), CollectionUtils.get(values, 1));
    }

    // ========== 重写父类方法，方便链式调用 ==========

    @Override
    public LambdaQueryWrapper<T> eq(boolean condition, SFunction<T, ?> column, Object val) {
        super.eq(condition, column, val);
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> eq(SFunction<T, ?> column, Object val) {
        super.eq(column, val);
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> orderByDesc(SFunction<T, ?> column) {
        super.orderByDesc(true, column);
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> orderByAsc(SFunction<T, ?> column) {
        super.orderByAsc(true, column);
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> last(String lastSql) {
        super.last(lastSql);
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> in(SFunction<T, ?> column, Collection<?> coll) {
        super.in(column, coll);
        return this;
    }

}
