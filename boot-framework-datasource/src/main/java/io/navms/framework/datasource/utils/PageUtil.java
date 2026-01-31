package io.navms.framework.datasource.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.navms.framework.common.base.utils.CollectionUtils;
import io.navms.framework.common.base.utils.StringUtils;
import io.navms.framework.datasource.domain.IPageable;
import io.navms.framework.datasource.domain.PageQuery;
import io.navms.framework.datasource.domain.Pageable;
import io.navms.framework.datasource.domain.SortField;

import java.util.function.Function;

/**
 * 分页工具类
 *
 * @author navms
 */
public class PageUtil {

    public static <T> IPage<T> buildPage(PageQuery query) {
        Page<T> page = new Page<>(query.getCurrent(), query.getSize());
        if (CollectionUtils.isNotEmpty(query.getOrders())) {
            for (SortField sortField : query.getOrders()) {
                page.addOrder(new OrderItem().setAsc(sortField.isAsc())
                        .setColumn(StringUtils.toUnderlineCase(sortField.getColumn())));
            }
        }
        return page;
    }

    public static <S, R> IPageable<R> convert(IPage<S> page, Function<S, R> mapping) {
        IPageable<R> result = new Pageable<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(page.getRecords().stream().map(mapping).toList());
        return result;
    }

    public static <S, R> IPageable<R> convert(IPageable<S> page, Function<S, R> mapping) {
        IPageable<R> result = new Pageable<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(page.getRecords().stream().map(mapping).toList());
        return result;
    }

}
