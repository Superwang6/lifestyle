package cn.fudges.server.service;

import cn.fudges.server.entity.ScheduleTask;
import cn.fudges.server.request.ScheduleTaskRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 定时任务 服务类
 * </p>
 *
 * @author wpy
 * @since 2025-07-02
 */
public interface ScheduleTaskService extends IService<ScheduleTask> {

    Long saveScheduleTask(ScheduleTaskRequest request);

    void removeScheduleTask(Long scheduleTaskId);
}
