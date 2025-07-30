package cn.fudges.server.service.impl;

import cn.fudges.server.config.properties.ProjectConfigProperties;
import cn.fudges.server.entity.bo.Configuration;
import cn.fudges.server.service.ConfigureService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author 王平远
 * @since 2025/7/28
 */
@Service
@RequiredArgsConstructor
public class ConfigureServiceImpl implements ConfigureService {

    private final ProjectConfigProperties projectConfigProperties;

    @Override
    public Configuration index(HttpServletRequest request, Long userId) {
        Configuration configuration = new Configuration();
        configuration.setDomain(projectConfigProperties.getDomain());
        configuration.setFilePrefix(projectConfigProperties.getFile().getPrePath());
        return configuration;
    }
}
