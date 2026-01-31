package io.navms.framework.datasource.utils;

import io.navms.framework.common.base.utils.Requires;
import io.navms.framework.common.base.utils.SpringContextHolder;
import io.navms.framework.datasource.exception.IdGenerateException;
import io.navms.framework.datasource.idgen.IdGenResult;
import io.navms.framework.datasource.idgen.IdGenerator;

/**
 * ID 生成工具类
 *
 * @author navms
 */
public class IdGenerateUtil {

    public static Long nextId(String key) {
        IdGenResult result = getIdGenerator().nextId(key);
        if (result.isSuccess()) {
            return result.getId();
        }
        throw new IdGenerateException(result.getStatus());
    }

    private static class IdGeneratorHolder {
        private static final IdGenerator INSTANCE = Requires.requireNotNull(
                SpringContextHolder.getApplicationContext().getBean(IdGenerator.class), "Id 生成器尚未加载");
    }

    public static IdGenerator getIdGenerator() {
        return IdGeneratorHolder.INSTANCE;
    }

}
