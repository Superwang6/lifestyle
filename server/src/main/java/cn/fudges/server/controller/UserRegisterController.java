package cn.fudges.server.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.request.UserPasswordRegisterRequest;
import cn.fudges.server.response.UserBaseResponse;
import cn.fudges.server.service.UserRegisterService;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 王平远
 * @since 2025/7/23
 */
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@SaIgnore
public class UserRegisterController {

    private final UserRegisterService userRegisterService;

    @PostMapping("/userPassword")
    public ResultResponse<UserBaseResponse> registerUserPassword(@RequestBody UserPasswordRegisterRequest request) {
        return ResultResponse.success(BeanUtil.copyProperties(userRegisterService.registerUserPassword(request), UserBaseResponse.class));
    }

}
