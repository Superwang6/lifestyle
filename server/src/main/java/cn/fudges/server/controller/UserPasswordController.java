package cn.fudges.server.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.request.UserPasswordRequest;
import cn.fudges.server.service.UserPasswordService;
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
}
