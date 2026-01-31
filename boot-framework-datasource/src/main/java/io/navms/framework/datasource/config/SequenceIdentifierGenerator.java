package io.navms.framework.datasource.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import io.navms.framework.datasource.utils.IdGenerateUtil;

/**
 * 号段模式 Id 生成器
 *
 * @author navms
 */
public class SequenceIdentifierGenerator implements IdentifierGenerator {

    @Override
    public Number nextId(Object entity) {
        String bizKey = entity.getClass().getSimpleName();
        return IdGenerateUtil.nextId(bizKey);
    }

}
