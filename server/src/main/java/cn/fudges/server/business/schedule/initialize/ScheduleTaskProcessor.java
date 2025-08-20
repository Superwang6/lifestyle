package cn.fudges.server.business.schedule.initialize;

import cn.fudges.server.business.schedule.entity.bo.ScheduleExecuteInfo;
import cn.fudges.server.business.schedule.enums.ScheduleTaskBusinessType;

/**
 * @author 王平远
 * @since 2025/7/3
 */
public interface ScheduleTaskProcessor {

    ScheduleTaskBusinessType getScheduleTaskBusinessType();

    void scheduleTaskExecute(ScheduleExecuteInfo executeInfo);
}
