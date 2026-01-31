package io.navms.framework.datasource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Datasource 模块配置类
 *
 * @author navms
 */
@Data
@ConfigurationProperties(prefix = "boot.framework.datasource")
public class DatasourceProperties {

    private String baseMapper;

}
