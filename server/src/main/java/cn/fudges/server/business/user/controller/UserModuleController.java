package cn.fudges.server.business.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.business.user.entity.ModuleInfo;
import cn.fudges.server.business.user.request.SaveUserModuleRequest;
import cn.fudges.server.business.user.request.UserModuleRequest;
import cn.fudges.server.business.user.response.UserModuleResponse;
import cn.fudges.server.business.user.service.UserModuleService;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.business.user.request.ModuleInfoRequest;
import cn.fudges.server.business.user.response.ModuleInfoResponse;
import cn.fudges.server.business.user.service.ModuleInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户模块关系表 前端控制器
 * </p>
 *
 * @author wpy
 * @since 2025-08-12
 */
@RestController
@RequestMapping("/userModule")
@RequiredArgsConstructor
public class UserModuleController {

    private final UserModuleService userModuleService;

    @PostMapping("/modules")
    public ResultResponse<List<ModuleInfoResponse>> queryUserSettingModules() {
        UserModuleRequest request = new UserModuleRequest();
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(userModuleService.queryUserSettingModules(request), ModuleInfoResponse.class);
    }

    @PostMapping("/save")
    public ResultResponse<Boolean> saveUserModule(@RequestBody SaveUserModuleRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(userModuleService.saveUserModule(request));
    }

    @PostMapping("/ownModules")
    public ResultResponse<List<UserModuleResponse>> queryOwnModules() {
        UserModuleRequest request = new UserModuleRequest();
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(userModuleService.queryOwnModules(request), UserModuleResponse.class);
    }
}
