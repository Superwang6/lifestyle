package cn.fudges.server.business.jot.queue;

import cn.fudges.server.common.RedisKeys;
import cn.fudges.server.business.jot.entity.JotRecord;
import cn.fudges.server.initialize.queue.QueueConsumer;
import cn.fudges.server.business.push.request.PushRecordRequest;
import cn.fudges.server.business.jot.entity.bo.JotRemindInfo;
import cn.fudges.server.business.jot.service.JotRecordService;
import cn.fudges.server.business.push.service.PushRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author 王平远
 * @since 2025/6/27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JotRemindMessageConsumer implements QueueConsumer<JotRemindInfo> {

    private final JotRecordService jotRecordService;

    private final PushRecordService pushRecordService;

    @Override
    public String queueName() {
        return RedisKeys.JOT_REMIND_SEND_MESSAGE_QUEUE;
    }

    @Override
    public void consume(JotRemindInfo info) {
        log.info("jotRemindListener accept: {}", info);
        Long jotRecordId = info.getJotRecordId();
        Boolean timesExhaustion = info.getTimesExhaustion();
        JotRecord record = jotRecordService.getById(jotRecordId);

        JotRecord update = new JotRecord();
        update.setId(record.getId());
        update.setModifyTime(LocalDateTime.now());
        try {
            PushRecordRequest pushRecordRequest = new PushRecordRequest();
            pushRecordRequest.setContent("【" + record.getTitle() + "】已到提醒时间！");
            pushRecordRequest.setTargetUserId(record.getUserId());
            boolean result = pushRecordService.sendPushRecord(pushRecordRequest);
            if(result) {
                if(timesExhaustion) {
                    update.setRemindStatus(2);
                } else {
                    update.setRemindStatus(4);
                }
                jotRecordService.updateById(update);
            }
        } catch (Exception e) {
            update.setRemindStatus(3);
            jotRecordService.updateById(update);
        }
    }
}
