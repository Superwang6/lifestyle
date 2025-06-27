package cn.fudges.server.controller;

import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.response.UserBaseResponse;
import cn.fudges.server.service.inner.UserBaseService;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/detail/{id}")
    public ResultResponse<UserBaseResponse> detail(@PathVariable Integer id) {
        return ResultResponse.success(BeanUtil.copyProperties(userBaseService.detail(id), UserBaseResponse.class));
    }
}
