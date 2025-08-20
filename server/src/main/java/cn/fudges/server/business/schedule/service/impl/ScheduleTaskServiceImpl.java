package cn.fudges.server.business.schedule.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.RedisKeys;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.business.schedule.entity.ScheduleTask;
import cn.fudges.server.business.schedule.entity.bo.ScheduleTaskChange;
import cn.fudges.server.business.schedule.mapper.ScheduleTaskMapper;
import cn.fudges.server.business.schedule.request.ScheduleTaskRequest;
import cn.fudges.server.business.schedule.service.ScheduleRecordService;
import cn.fudges.server.business.schedule.service.ScheduleTaskService;
import cn.fudges.server.utils.AssertUtils;
import cn.fudges.server.utils.CronUtils;
import cn.fudges.server.utils.queue.QueueUtils;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 定时任务 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2025-07-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleTaskServiceImpl extends ServiceImpl<ScheduleTaskMapper, ScheduleTask> implements ScheduleTaskService {

    private final ScheduleRecordService scheduleRecordService;

    private final QueueUtils queueUtils;

    @Override
    public Long saveScheduleTask(ScheduleTaskRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getCron(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getName(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getUserId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getBusinessType(), ResultCodeEnum.PARAM_ERROR);

        AssertUtils.isTrue(CronUtils.isValid(request.getCron()), ResultCodeEnum.PARAM_ERROR, "cron表达式非法！");

        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setCron(request.getCron());
        scheduleTask.setName(request.getName());
        scheduleTask.setUserId(request.getUserId());
        scheduleTask.setModifyTime(LocalDateTime.now());
        scheduleTask.setBusinessType(request.getBusinessType().getCode());
        scheduleTask.setTriggerTimes(request.getTriggerTimes());
        if(ObjectUtil.isNotNull(request.getId())) {
            ScheduleTask oldTask = getById(request.getId());
            AssertUtils.isNotNull(oldTask, ResultCodeEnum.PARAM_ERROR);
            AssertUtils.isTrue(oldTask.getUserId().equals(request.getUserId()), ResultCodeEnum.PERMISSION_DENIED);
            scheduleTask.setId(oldTask.getId());
            updateById(scheduleTask);
        } else {
            scheduleTask.setCreateTime(LocalDateTime.now());
            save(scheduleTask);
        }


        // 保存定时任务
        ScheduleTaskChange change = new ScheduleTaskChange();
        change.setId(scheduleTask.getId());
        change.setChangeType(ScheduleTaskChange.SAVE);
        change.setCron(scheduleTask.getCron());
        queueUtils.sendMessage(RedisKeys.SCHEDULE_TASK_CHANGE_QUEUE, change);

        return scheduleTask.getId();
    }

    @Transactional
    @Override
    public void removeScheduleTask(Long scheduleTaskId) {
        AssertUtils.isNotNull(scheduleTaskId, ResultCodeEnum.PARAM_ERROR);

        ScheduleTask oldTask = getById(scheduleTaskId);
        AssertUtils.isTrue(oldTask.getUserId().equals(StpUtil.getLoginIdAsLong()), ResultCodeEnum.PERMISSION_DENIED);
        removeById(scheduleTaskId);

        scheduleRecordService.removeByTaskId(scheduleTaskId);

        // 移除定时任务
        ScheduleTaskChange change = new ScheduleTaskChange();
        change.setId(scheduleTaskId);
        change.setChangeType(ScheduleTaskChange.DELETE);
        queueUtils.sendMessage(RedisKeys.SCHEDULE_TASK_CHANGE_QUEUE, change);
    }
}
