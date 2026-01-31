package io.navms.framework.common.base.utils;

/**
 * 三元函数式接口
 *
 * @author navms
 */
@FunctionalInterface
public interface TriConsumer<ONE, TWO, THREE> {

    /**
     * 抽象方法
     *
     * @param one   第一个参数
     * @param two   第二个参数
     * @param three 第三个参数
     */
    void accept(ONE one, TWO two, THREE three);

}
