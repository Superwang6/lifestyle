package cn.fudges.server.business.user.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.business.user.request.UserPasswordRequest;
import cn.fudges.server.business.user.request.UserSmsRequest;
import cn.fudges.server.business.user.response.UserLoginResponse;
import cn.fudges.server.business.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 王平远
 * @since 2025/5/24
 */
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@SaIgnore
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/password")
    public ResultResponse<UserLoginResponse> loginByPassword(@RequestBody UserPasswordRequest request) {
        return ResultResponse.success(loginService.loginByPassword(request), UserLoginResponse.class);
    }

    @PostMapping("/mobilePhone")
    public ResultResponse<UserLoginResponse> loginByMobilePhone(@RequestBody UserSmsRequest request) {
        return ResultResponse.success(loginService.loginByMobilePhone(request), UserLoginResponse.class);
    }
}
