package io.navms.framework.web.config;

import io.navms.framework.common.base.log.LogUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Web 模块自动配置类
 *
 * @author navms
 */
@AutoConfiguration
@ComponentScan(basePackages = "io.navms.framework.web")
public class WebAutoConfiguration {

    public WebAutoConfiguration() {
        LogUtils.info("==============>>>>>>> Boot Framework Web 模块已启用");
    }

}
