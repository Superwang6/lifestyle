package cn.fudges.server.business.common.service;

import cn.fudges.server.business.common.entity.bo.Configuration;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author 王平远
 * @since 2025/7/28
 */

public interface ConfigureService {

    Configuration index(HttpServletRequest request, Long userId);
}
