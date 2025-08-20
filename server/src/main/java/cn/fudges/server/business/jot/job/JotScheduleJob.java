package cn.fudges.server.business.jot.job;

import cn.fudges.server.business.jot.service.JotRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 王平远
 * @since 2025/6/26
 */
@Component
@RequiredArgsConstructor
public class JotScheduleJob {

    private final JotRecordService jotRecordService;

    @Scheduled(cron = "10 0/1 * * * ?")
    public void jotRemindSchedule() {
        jotRecordService.jotRemindJob();
    }
}
