package cn.fudges.server.business.user.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.business.user.request.UserPasswordRequest;
import cn.fudges.server.business.user.service.UserPasswordService;
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
@RequestMapping("/userPassword")
@RequiredArgsConstructor
public class UserPasswordController {

    private final UserPasswordService userPasswordService;

    @PostMapping("/modifyPassword")
    public ResultResponse<Boolean> modifyPassword(@RequestBody UserPasswordRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(userPasswordService.modifyUserPassword(request));
    }

    @PostMapping("/forgetPassword")
    @SaIgnore
    public ResultResponse<Boolean> forgetPassword(@RequestBody UserPasswordRequest request) {
        return ResultResponse.success(userPasswordService.forgetPassword(request));
    }
}
