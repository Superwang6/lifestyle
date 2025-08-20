package cn.fudges.server.business.jot.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.business.jot.request.JotClassifyRequest;
import cn.fudges.server.business.jot.response.JotClassifyResponse;
import cn.fudges.server.business.jot.service.JotClassifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 备忘录分类 前端控制器
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
@RestController
@RequestMapping("/jotClassify")
@RequiredArgsConstructor
public class JotClassifyController {

    private final JotClassifyService jotClassifyService;

    @PostMapping("/page")
    public ResultResponse<List<JotClassifyResponse>> queryPage(@RequestBody JotClassifyRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(jotClassifyService.queryPage(request), JotClassifyResponse.class);
    }

    @PostMapping("/add")
    public ResultResponse<Boolean> add(@RequestBody JotClassifyRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(jotClassifyService.add(request));
    }

    @PostMapping("/modify")
    public ResultResponse<Boolean> modify(@RequestBody JotClassifyRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(jotClassifyService.modify(request));
    }

    @PostMapping("/delete/{id}")
    public ResultResponse<Boolean> delete(@PathVariable Long id) {
        return ResultResponse.success(jotClassifyService.delete(id, StpUtil.getLoginIdAsLong()));
    }
}
