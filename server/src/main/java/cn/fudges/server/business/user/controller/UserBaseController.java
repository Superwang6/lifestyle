package cn.fudges.server.business.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.business.user.request.UserBaseRequest;
import cn.fudges.server.business.user.response.UserBaseResponse;
import cn.fudges.server.business.user.service.UserBaseService;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
@RestController
@RequestMapping("/userBase")
@RequiredArgsConstructor
public class UserBaseController {

    private final UserBaseService userBaseService;

    @PostMapping("/detail")
    public ResultResponse<UserBaseResponse> detail() {
        Long userId = StpUtil.getLoginIdAsLong();
        return ResultResponse.success(BeanUtil.copyProperties(userBaseService.detail(userId), UserBaseResponse.class));
    }
    @PostMapping("/mobilePhone")
    public ResultResponse<String> queryMobilePhone() {
        Long userId = StpUtil.getLoginIdAsLong();
        return ResultResponse.success(userBaseService.queryMobilePhone(userId));
    }

    @PostMapping("/modify")
    public ResultResponse<Boolean> modify(@RequestBody UserBaseRequest request) {
        request.setId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(userBaseService.modifyUserBase(request));
    }

    @PostMapping("/modify/account")
    public ResultResponse<Boolean> modifyAccount(@RequestBody UserBaseRequest request) {
        request.setId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(userBaseService.modifyAccount(request));
    }

    @PostMapping("/modify/mobilePhone")
    public ResultResponse<Boolean> modifyMobilePhone(@RequestBody UserBaseRequest request) {
        request.setId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(userBaseService.modifyMobilePhone(request));
    }
}
