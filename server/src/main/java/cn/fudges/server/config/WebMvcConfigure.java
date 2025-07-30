package cn.fudges.server.config;

import cn.fudges.server.config.properties.ProjectConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author  王平远
 * @since  2025/7/24
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfigure implements WebMvcConfigurer {

    private final ProjectConfigProperties projectConfigProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(projectConfigProperties.getFile().getPrePath() + "/**").addResourceLocations("file:" + projectConfigProperties.getFile().getUploadDir());
    }
}
