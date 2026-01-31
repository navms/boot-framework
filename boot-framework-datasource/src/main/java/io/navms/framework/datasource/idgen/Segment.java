package io.navms.framework.datasource.idgen;

import io.navms.framework.datasource.idgen.entity.IdSegment;
import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 号段对象，包含当前可用区间 [value, max)
 *
 * @author navms
 */
@Data
public class Segment {

    /**
     * 当前的 Id 值
     */
    private AtomicLong value;

    /**
     * 号段最大值
     */
    private volatile long max;

    /**
     * 步长
     */
    private volatile int step;

    /**
     * 号段关联的双 Buffer
     */
    private SegmentBuffer buffer;

    public Segment(SegmentBuffer buffer) {
        this.buffer = buffer;
        this.max = 0;
        this.step = 0;
        this.value = new AtomicLong(0);
    }

    /**
     * 剩余可用的 Id 数
     */
    public long getIdle() {
        return this.getMax() - getValue().get();
    }

    /**
     * 将数据库号段更新结果应用到内存 segment 中
     *
     * @param idSegment 数据库的号段
     */
    public void applyToSegment(IdSegment idSegment) {
        // must set value before set max
        long value = idSegment.getMaxId() - buffer.getStep();
        this.value.set(value);
        this.max = idSegment.getMaxId();
        this.step = buffer.getStep();
    }

    @Override
    public String toString() {
        return "Segment{" +
                "value=" + value +
                ", max=" + max +
                ", step=" + step +
                '}';
    }

}
