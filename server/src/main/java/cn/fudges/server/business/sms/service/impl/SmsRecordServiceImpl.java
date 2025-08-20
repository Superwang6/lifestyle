package cn.fudges.server.business.sms.service.impl;

import cn.fudges.server.business.sms.entity.SmsRecord;
import cn.fudges.server.business.sms.mapper.SmsRecordMapper;
import cn.fudges.server.business.sms.request.SmsRecordRequest;
import cn.fudges.server.business.sms.service.SmsRecordService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 短信发送记录 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2025-08-06
 */
@Service
public class SmsRecordServiceImpl extends ServiceImpl<SmsRecordMapper, SmsRecord> implements SmsRecordService {

    @Override
    public void saveRecord(SmsRecordRequest recordRequest) {
        SmsRecord smsRecord = BeanUtil.copyProperties(recordRequest, SmsRecord.class);
        smsRecord.setCreateTime(LocalDateTime.now());
        save(smsRecord);
    }
}
