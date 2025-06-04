package cn.fudges.server.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.request.JotRecordRequest;
import cn.fudges.server.response.JotRecordResponse;
import cn.fudges.server.service.JotRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 备忘记录 前端控制器
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
@RestController
@RequestMapping("/jotRecord")
@RequiredArgsConstructor
public class JotRecordController {

    private final JotRecordService jotRecordService;

    @PostMapping("/page")
    public ResultResponse<List<JotRecordResponse>> queryPage(@RequestBody JotRecordRequest request) {
        return ResultResponse.success(jotRecordService.queryPage(request), JotRecordResponse.class);
    }

    @PostMapping("/add")
    public ResultResponse<Boolean> addJotRecord(@RequestBody JotRecordRequest request) {
        return ResultResponse.success(jotRecordService.addJotRecord(request));
    }

    @PostMapping("/modify")
    public ResultResponse<Boolean> modifyJotRecord(@RequestBody JotRecordRequest request) {
        return ResultResponse.success(jotRecordService.modifyJotRecord(request));
    }

    @PostMapping("/delete/{id}")
    public ResultResponse<Boolean> delete(@PathVariable String id) {
        return ResultResponse.success(jotRecordService.delete(id));
    }

    @PostMapping("/delay")
    public ResultResponse<Boolean> delay(@RequestBody JotRecordRequest request) {
        return ResultResponse.success(jotRecordService.delay(request));
    }
}
