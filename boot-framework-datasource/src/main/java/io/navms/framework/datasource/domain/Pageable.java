package io.navms.framework.datasource.domain;

import java.util.Collections;
import java.util.List;

/**
 * 分页模型
 *
 * @author navms
 */
public class Pageable<T> implements IPageable<T> {

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    private long total = 0;

    /**
     * 每页显示条数，默认 10
     */
    private long size = 10;

    /**
     * 当前页
     */
    private long current = 1;

    public Pageable() {
    }

    public Pageable(long current, long size) {
        this(current, size, 0);
    }

    public Pageable(long current, long size, long total) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
    }

    @Override
    public List<SortField> orders() {
        return List.of();
    }

    @Override
    public List<T> getRecords() {
        return records;
    }

    @Override
    public IPageable<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public IPageable<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public IPageable<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return current;
    }

    @Override
    public IPageable<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

}
