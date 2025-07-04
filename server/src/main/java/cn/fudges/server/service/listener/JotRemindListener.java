package cn.fudges.server.service.listener;

import cn.fudges.server.entity.JotRecord;
import cn.fudges.server.enums.JotRemindTypeEnum;
import cn.fudges.server.request.PushRecordRequest;
import cn.fudges.server.service.event.JotRemindEvent;
import cn.fudges.server.service.JotRecordService;
import cn.fudges.server.service.PushRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author 王平远
 * @since 2025/6/27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JotRemindListener {

    private final JotRecordService jotRecordService;

    private final PushRecordService pushRecordService;


    @Async
    @EventListener
    public void jotRemind(JotRemindEvent event) {
        log.info("jotRemindListener accept: {}", event);
        Long jotRecordId = event.getJotRecordId();
        Boolean timesExhaustion = event.getTimesExhaustion();
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
