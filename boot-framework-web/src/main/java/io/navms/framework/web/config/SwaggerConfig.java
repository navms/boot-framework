package io.navms.framework.web.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Swagger 配置
 *
 * @author navms
 */
@EnableKnife4j
@Configuration
@ConditionalOnProperty(prefix = "swagger", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfig {

    @Resource
    private SwaggerProperties properties;

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openApi = new OpenAPI();

        // 设置基本信息
        Info info = new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion());

        // 设置联系人信息
        if (properties.getContact() != null) {
            SwaggerProperties.Contact contactConfig = properties.getContact();
            Contact contact = new Contact()
                    .name(contactConfig.getName())
                    .email(contactConfig.getEmail())
                    .url(contactConfig.getUrl());
            info.contact(contact);
        }

        // 设置许可证信息
        if (properties.getLicense() != null) {
            SwaggerProperties.License licenseConfig = properties.getLicense();
            License license = new License()
                    .name(licenseConfig.getName())
                    .url(licenseConfig.getUrl());
            info.license(license);
        }

        openApi.info(info);

        // 设置服务器列表
        if (properties.getServers() != null && !properties.getServers().isEmpty()) {
            List<Server> servers = properties.getServers().stream()
                    .map(s -> new Server().url(s.getUrl()).description(s.getDescription()))
                    .collect(Collectors.toList());
            openApi.servers(servers);
        }

        return openApi;
    }

}

