package cn.fudges.server.business.sms.service;

import cn.fudges.server.business.sms.entity.SmsRecord;
import cn.fudges.server.business.sms.request.SmsRecordRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 短信发送记录 服务类
 * </p>
 *
 * @author wpy
 * @since 2025-08-06
 */
public interface SmsRecordService extends IService<SmsRecord> {

    void saveRecord(SmsRecordRequest recordRequest);
}
