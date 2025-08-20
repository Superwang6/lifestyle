package cn.fudges.server.business.jot.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.business.jot.request.JotBookRequest;
import cn.fudges.server.business.jot.response.JotBookResponse;
import cn.fudges.server.business.jot.service.JotBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 备忘本 前端控制器
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
@RestController
@RequestMapping("/jotBook")
@RequiredArgsConstructor
public class JotBookController {

    private final JotBookService jotBookService;

    @PostMapping("/page")
    public ResultResponse<List<JotBookResponse>> queryPage(@RequestBody JotBookRequest request) {
        return ResultResponse.success(jotBookService.queryPage(request), JotBookResponse.class);
    }

    @PostMapping("/add")
    public ResultResponse<Boolean> addJotBook(@RequestBody JotBookRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(jotBookService.addJotBook(request));
    }

    @PostMapping("/modify")
    public ResultResponse<Boolean> modifyJotBook(@RequestBody JotBookRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(jotBookService.modifyJotBook(request));
    }

    @PostMapping("/delete/{id}")
    public ResultResponse<Boolean> deleteJotBook(@PathVariable("id") Long id) {
        return ResultResponse.success(jotBookService.deleteJotBook(id, StpUtil.getLoginIdAsLong()));
    }

}
