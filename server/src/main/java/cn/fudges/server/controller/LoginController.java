package cn.fudges.server.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.request.UserPasswordRequest;
import cn.fudges.server.response.UserLoginResponse;
import cn.fudges.server.service.LoginService;
import cn.fudges.server.service.UserPasswordService;
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
        return ResultResponse.success(loginService.loginByPassword(request));
    }
}
