package cn.fudges.server.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.request.UserBaseRequest;
import cn.fudges.server.response.UserBaseResponse;
import cn.fudges.server.service.UserBaseService;
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

    @PostMapping("/detail/{id}")
    public ResultResponse<UserBaseResponse> detail(@PathVariable Integer id) {
        return ResultResponse.success(BeanUtil.copyProperties(userBaseService.detail(id), UserBaseResponse.class));
    }

    @PostMapping("/modify")
    public ResultResponse<Boolean> modify(@RequestBody UserBaseRequest request) {
        request.setId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(userBaseService.modifyUserBase(request));
    }
}
