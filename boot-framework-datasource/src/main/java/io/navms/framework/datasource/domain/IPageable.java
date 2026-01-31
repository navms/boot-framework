package io.navms.framework.datasource.domain;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页接口
 *
 * @author navms
 */
public interface IPageable<T> {

    /**
     * 获取排序信息，排序的字段和正反序
     *
     * @return 排序信息
     */
    List<SortField> orders();

    /**
     * 计算当前分页偏移量
     *
     * @return 偏移量
     */
    default long offset() {
        long current = getCurrent();
        if (current <= 1L) {
            return 0L;
        }
        return Math.max((current - 1) * getSize(), 0L);
    }

    /**
     * 当前分页总页数
     *
     * @return 总页数
     */
    default long getPages() {
        if (getSize() == 0) {
            return 0L;
        }
        long pages = getTotal() / getSize();
        if (getTotal() % getSize() != 0) {
            pages++;
        }
        return pages;
    }

    /**
     * 是否有上一页
     *
     * @return 是否有上一页
     */
    default boolean hasPrevious() {
        return this.getCurrent() > 1;
    }

    /**
     * 是否有下一页
     *
     * @return 是否有下一页
     */
    default boolean hasNext() {
        return this.getCurrent() < this.getPages();
    }

    /**
     * 添加排序字段
     *
     * @param fields 排序字段
     * @return IPageable
     */
    default IPageable<T> addOrder(SortField... fields) {
        orders().addAll(Arrays.asList(fields));
        return this;
    }

    /**
     * 添加排序字段
     *
     * @param fields 排序字段
     * @return 返回分页参数本身
     */
    default IPageable<T> addOrder(List<SortField> fields) {
        orders().addAll(fields);
        return this;
    }

    /**
     * 分页记录列表
     *
     * @return 分页对象记录列表
     */
    List<T> getRecords();

    /**
     * 设置分页记录列表
     */
    IPageable<T> setRecords(List<T> records);

    /**
     * 当前满足条件总行数
     *
     * @return 总条数
     */
    long getTotal();

    /**
     * 设置当前满足条件总行数
     *
     * @param total 总条数
     */
    IPageable<T> setTotal(long total);

    /**
     * 获取每页显示条数
     *
     * @return 每页显示条数
     */
    long getSize();

    /**
     * 设置每页显示条数
     *
     * @param size 每页显示条数
     */
    IPageable<T> setSize(long size);

    /**
     * 当前页
     *
     * @return 当前页
     */
    long getCurrent();

    /**
     * 设置当前页
     *
     * @param current 当前页
     */
    IPageable<T> setCurrent(long current);

    /**
     * IPageable 的泛型转换
     *
     * @param mapping 转换函数
     * @param <R>     转换后的泛型
     * @return 转换泛型后的 IPageable
     */
    @SuppressWarnings("unchecked")
    default <R> IPageable<R> convert(Function<? super T, ? extends R> mapping) {
        List<R> collect = this.getRecords().stream().map(mapping).collect(Collectors.toList());
        return ((IPageable<R>) this).setRecords(collect);
    }

}
