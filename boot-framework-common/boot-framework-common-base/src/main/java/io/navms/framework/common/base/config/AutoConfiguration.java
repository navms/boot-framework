package io.navms.framework.common.base.config;

import io.navms.framework.common.base.log.LogUtils;
import io.navms.framework.common.base.log.aspect.LogAspect;
import io.navms.framework.common.base.utils.SpringContextHolder;
import org.springframework.context.annotation.Bean;

/**
 * Common Base 模块自动配置类
 *
 * @author navms
 */
@org.springframework.boot.autoconfigure.AutoConfiguration
public class AutoConfiguration {

    public AutoConfiguration() {
        LogUtils.info("==============>>>>>>> Boot Framework Common Base 模块已启用");
    }

    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

}
