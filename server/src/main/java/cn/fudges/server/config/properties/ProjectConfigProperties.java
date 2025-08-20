package cn.fudges.server.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * @author 王平远
 * @since 2025/7/25
 */
@Data
@Component
@ConfigurationProperties(prefix = "project")
public class ProjectConfigProperties {

    private String domain;

    @NestedConfigurationProperty
    private SupportProperties support;

    @NestedConfigurationProperty
    private FileConfigProperties file;
}
