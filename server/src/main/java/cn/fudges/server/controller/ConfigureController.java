package cn.fudges.server.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.response.ConfigurationResponse;
import cn.fudges.server.service.ConfigureService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 王平远
 * @since 2025/7/28
 */
@RestController
@RequestMapping("/configure")
@RequiredArgsConstructor
public class ConfigureController {

    private final ConfigureService configureService;

    @PostMapping("/index")
    @SaIgnore
    public ResultResponse<ConfigurationResponse> index(HttpServletRequest request) {
        return ResultResponse.success(configureService.index(request, StpUtil.isLogin() ? StpUtil.getLoginIdAsLong(): null), ConfigurationResponse.class);
    }
}
