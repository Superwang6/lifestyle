package cn.fudges.server.business.jot.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.business.jot.entity.JotRecord;
import cn.fudges.server.business.jot.request.JotRecordRequest;
import cn.fudges.server.business.jot.response.JotRecordResponse;
import cn.fudges.server.business.jot.service.JotRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
        request.setUserId(StpUtil.getLoginIdAsLong());
        IPage<JotRecord> page = jotRecordService.queryPage(request);
        return ResultResponse.success(page, JotRecordResponse.class);
    }

    @PostMapping("/add")
    public ResultResponse<Boolean> addJotRecord(@RequestBody JotRecordRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(jotRecordService.addJotRecord(request));
    }

    @PostMapping("/modify")
    public ResultResponse<Boolean> modifyJotRecord(@RequestBody JotRecordRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(jotRecordService.modifyJotRecord(request));
    }

    @PostMapping("/modifyStatus")
    public ResultResponse<Boolean> modifyStatus(@RequestBody JotRecordRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(jotRecordService.modifyStatus(request));
    }

    @PostMapping("/delete/{id}")
    public ResultResponse<Boolean> delete(@PathVariable Long id) {
        JotRecordRequest request = new JotRecordRequest();
        request.setId(id);
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(jotRecordService.delete(request));
    }

    @PostMapping("/delay")
    public ResultResponse<Boolean> delay(@RequestBody JotRecordRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        return ResultResponse.success(jotRecordService.delay(request));
    }
}
