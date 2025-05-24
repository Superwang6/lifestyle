package cn.fudges.server.controller;

import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.request.JotBookRequest;
import cn.fudges.server.response.JotBookResponse;
import cn.fudges.server.service.JotBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResultResponse.success(jotBookService.addJotBook(request));
    }

}
