package io.navms.framework.common.idempotent.config;

import io.navms.framework.common.base.log.LogUtils;
import io.navms.framework.common.idempotent.aspect.IdempotentAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Common Idempotent 模块自动配置类
 *
 * @author navms
 */
@AutoConfiguration
public class IdempotentAutoConfiguration {

    public IdempotentAutoConfiguration() {
        LogUtils.info("==============>>>>>>> Boot Framework Common Idempotent 模块已启用");
    }

    @Bean
    public IdempotentAspect idempotentAspect() {
        return new IdempotentAspect();
    }

}
