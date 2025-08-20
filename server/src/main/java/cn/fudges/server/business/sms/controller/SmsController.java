package cn.fudges.server.business.sms.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.business.sms.request.SendSmsRequest;
import cn.fudges.server.business.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 王平远
 * @since 2025/8/6
 */
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/send/code")
    @SaIgnore
    public ResultResponse<Boolean> sendSmsCode(@RequestBody SendSmsRequest request) {
        return ResultResponse.success(smsService.sendSmsCode(request));
    }
}
