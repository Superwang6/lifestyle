package cn.fudges.server.business.sms.service.impl;

import cn.fudges.server.common.RedisKeys;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.business.sms.enums.SmsBusinessEnum;
import cn.fudges.server.business.sms.request.SendSmsRequest;
import cn.fudges.server.business.sms.request.SmsRecordRequest;
import cn.fudges.server.business.sms.service.SmsRecordService;
import cn.fudges.server.business.sms.service.SmsService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author 王平远
 * @since 2025/8/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final SmsRecordService smsRecordService;

    private final RedissonClient redissonClient;

    @Override
    public Boolean sendSmsCode(SendSmsRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotBlank(request.getMobilePhone(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isTrue(PhoneUtil.isMobile(request.getMobilePhone()), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotBlank(request.getBusiness(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isTrue(SmsBusinessEnum.check(request.getBusiness()), ResultCodeEnum.PARAM_ERROR);

        String sign = "节奏生活";
        String code = RandomUtil.randomNumbers(6);
        String content = "您的验证码是：" + code + "，验证码有效期5分钟，请勿将验证码交予他人。如非本人操作，请忽略此短信。";

        offerSms(request.getMobilePhone(), sign, content);

        RMapCache<Object, Object> businessSmsMap = redissonClient.getMapCache(RedisKeys.SMS_SEND_KEY + request.getBusiness());
        businessSmsMap.put(request.getMobilePhone(), code, 5 * 60, TimeUnit.SECONDS);

        SmsRecordRequest recordRequest = new SmsRecordRequest();
        recordRequest.setMobilePhone(request.getMobilePhone());
        recordRequest.setSign(sign);
        recordRequest.setMessage("【" + sign + "】" + content);
        recordRequest.setSourceUserId(-1L);
        recordRequest.setSourceUserName("admin");
        recordRequest.setBusiness(request.getBusiness());
        smsRecordService.saveRecord(recordRequest);
        log.info("send login sms success, mobilePhone:{}, content:{}", request.getMobilePhone(), content);
        return true;
    }

    @Override
    public boolean checkSmsCode(String mobilePhone, SmsBusinessEnum smsBusinessEnum, String code) {
        RMapCache<String, String> mapCache = redissonClient.getMapCache(RedisKeys.SMS_SEND_KEY + smsBusinessEnum.name());
        String checkCode = mapCache.get(mobilePhone);
        AssertUtils.isTrue(StrUtil.equals(checkCode, code), ResultCodeEnum.SMS_CODE_ERROR);
        mapCache.remove(mobilePhone);
        return true;
    }

    private void offerSms(String mobilePhone, String sign, String content) {
        // TODO 发送短信，暂无短信渠道，此处仅为演示
    }
}
