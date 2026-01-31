package io.navms.framework.datasource.idgen;

/**
 * ID 生成器接口
 *
 * @author navms
 */
public interface IdGenerator {

    /**
     * 初始化
     */
    void init();

    /**
     * 生成下一个 ID
     *
     * @param key 键
     * @return 生成的 ID
     */
    IdGenResult nextId(String key);

}
