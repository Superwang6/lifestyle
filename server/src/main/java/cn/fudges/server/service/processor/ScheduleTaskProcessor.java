package cn.fudges.server.service.processor;

import cn.fudges.server.enums.ScheduleTaskBusinessType;

import java.util.Map;

/**
 * @author 王平远
 * @since 2025/7/3
 */
public interface ScheduleTaskProcessor {

    ScheduleTaskBusinessType getScheduleTaskBusinessType();

    void scheduleTaskExecute(Long taskId, Map<String,Object> extendFields);
}
