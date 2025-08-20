package cn.fudges.server.business.sms.service;

import cn.fudges.server.business.sms.enums.SmsBusinessEnum;
import cn.fudges.server.business.sms.request.SendSmsRequest;

/**
 * @author 王平远
 * @since 2025/8/6
 */

public interface SmsService {

    Boolean sendSmsCode(SendSmsRequest request);

    boolean checkSmsCode(String mobilePhone, SmsBusinessEnum smsBusinessEnum, String code);
}
