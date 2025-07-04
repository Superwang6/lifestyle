package cn.fudges.server.service;

import cn.fudges.server.entity.ScheduleRecord;
import cn.fudges.server.request.ScheduleRecordRequest;
import cn.fudges.server.service.processor.ScheduleTaskProcessor;
import cn.hutool.cron.task.Task;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 定时任务触发记录 服务类
 * </p>
 *
 * @author wpy
 * @since 2025-07-02
 */
public interface ScheduleRecordService extends IService<ScheduleRecord> {

    void removeByTaskId(Long scheduleTaskId);

    Map<Long, Integer> queryTriggerTimesByTaskIdList(List<Long> taskIdList);
}
