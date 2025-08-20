package cn.fudges.server.business.schedule.service;

import cn.fudges.server.business.schedule.entity.ScheduleRecord;
import com.baomidou.mybatisplus.extension.service.IService;

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

    Integer queryTriggerTimeByTaskId(Long taskId);
}
