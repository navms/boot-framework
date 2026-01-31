package io.navms.framework.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger 配置属性
 *
 * @author navms
 */
@Data
@Component
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    /**
     * 是否启用 Swagger
     */
    private Boolean enabled = true;

    /**
     * API 文档标题
     */
    private String title = "API 文档";

    /**
     * API 文档描述
     */
    private String description = "API 接口文档";

    /**
     * API 文档版本
     */
    private String version = "1.0.0";

    /**
     * 联系人信息
     */
    private Contact contact = new Contact();

    /**
     * 许可证信息
     */
    private License license = new License();

    /**
     * 服务器列表
     */
    private List<Server> servers = new ArrayList<>();

    /**
     * 联系人信息
     */
    @Data
    public static class Contact {
        /**
         * 联系人姓名
         */
        private String name = "";

        /**
         * 联系人邮箱
         */
        private String email = "";

        /**
         * 联系人 URL
         */
        private String url = "";
    }

    /**
     * 许可证信息
     */
    @Data
    public static class License {
        /**
         * 许可证名称
         */
        private String name = "Apache 2.0";

        /**
         * 许可证 URL
         */
        private String url = "https://www.apache.org/licenses/LICENSE-2.0.html";
    }

    /**
     * 服务器信息
     */
    @Data
    public static class Server {
        /**
         * 服务器 URL
         */
        private String url;

        /**
         * 服务器描述
         */
        private String description;
    }

}
