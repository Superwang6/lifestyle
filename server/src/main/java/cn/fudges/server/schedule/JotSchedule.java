package cn.fudges.server.schedule;

import cn.fudges.server.service.JotRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 王平远
 * @since 2025/6/26
 */
@Component
@RequiredArgsConstructor
public class JotSchedule {

    private final JotRecordService jotRecordService;

    @Async
    @Scheduled(cron = "10 0/1 * * * ?")
    public void jotRemindSchedule() {
        jotRecordService.jotRemindJob();
    }
}
