package cn.fudges.server.business.schedule.initialize;

import cn.fudges.server.common.RedisKeys;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.business.schedule.entity.ScheduleRecord;
import cn.fudges.server.business.schedule.entity.ScheduleTask;
import cn.fudges.server.business.schedule.entity.bo.ScheduleExecuteInfo;
import cn.fudges.server.business.schedule.entity.bo.ScheduleTaskChange;
import cn.fudges.server.initialize.queue.QueueConsumer;
import cn.fudges.server.business.schedule.service.ScheduleRecordService;
import cn.fudges.server.business.schedule.service.ScheduleTaskService;
import cn.fudges.server.utils.AssertUtils;
import cn.fudges.server.utils.queue.QueueUtils;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 王平远
 * @since 2025/8/6
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleTaskInitialize implements SmartInitializingSingleton, QueueConsumer<ScheduleTaskChange> {

    private final String SCHEDULE_TASK_ID_PREFIX = "schedule_task_";

    private final Map<Integer, ScheduleTaskProcessor> PROCESSOR_MAP = new HashMap<>();

    private final ScheduleRecordService scheduleRecordService;

    private final ScheduleTaskService scheduleTaskService;

    private final ApplicationContext applicationContext;

    private final QueueUtils queueUtils;

    /**
     * 初始化cron任务
     */
    @Override
    public void afterSingletonsInstantiated() {
        Map<String, ScheduleTaskProcessor> processorMap = applicationContext.getBeansOfType(ScheduleTaskProcessor.class);
        for (ScheduleTaskProcessor processor : processorMap.values()) {
            PROCESSOR_MAP.put(processor.getScheduleTaskBusinessType().getCode(), processor);
        }

        LambdaQueryWrapper<ScheduleTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScheduleTask::getStatus, 0);
        List<ScheduleTask> list = scheduleTaskService.list(queryWrapper);
        for (ScheduleTask scheduleTask : list) {
            ScheduleTaskChange change = new ScheduleTaskChange();
            change.setId(scheduleTask.getId());
            change.setChangeType(ScheduleTaskChange.SAVE);
            change.setCron(scheduleTask.getCron());
            queueUtils.sendMessage(RedisKeys.SCHEDULE_TASK_CHANGE_QUEUE, change);
        }

        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    public Task executeSchedule(Long taskId) {
        AssertUtils.isNotNull(taskId, ResultCodeEnum.PARAM_ERROR);
        return () -> {
            try {
                MDC.put("traceId", IdUtil.fastSimpleUUID());
                log.info("执行定时任务：taskId:{}", taskId);
                ScheduleTask scheduleTask = scheduleTaskService.getById(taskId);
                if (ObjectUtil.isNotNull(scheduleTask)) {
                    // 次数是否耗尽
                    boolean timesExhaustion = false;
                    if (ObjectUtil.isNotNull(scheduleTask.getTriggerTimes()) && scheduleTask.getTriggerTimes() > 0) {
                        Integer count = scheduleRecordService.queryTriggerTimeByTaskId(taskId);
                        if (scheduleTask.getTriggerTimes() <= count) {
                            timesExhaustion = true;
                        }
                    }
                    if (!timesExhaustion) {
                        ScheduleRecord scheduleRecord = new ScheduleRecord();
                        try {
                            ScheduleTaskProcessor processor = PROCESSOR_MAP.get(scheduleTask.getBusinessType());
                            if (ObjectUtil.isNotNull(processor)) {
                                ScheduleExecuteInfo info = new ScheduleExecuteInfo();
                                info.setTaskId(scheduleTask.getId());
                                info.setTimesExhaustion(timesExhaustion);
                                processor.scheduleTaskExecute(info);

                                scheduleRecord.setResult(0);
                            } else {
                                scheduleRecord.setResult(1);
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                            scheduleRecord.setResult(1);
                        } finally {
                            scheduleRecord.setScheduleTaskId(scheduleTask.getId());
                            scheduleRecord.setUserId(scheduleTask.getUserId());
                            scheduleRecord.setCreateTime(LocalDateTime.now());
                            scheduleRecord.setBusinessType(scheduleTask.getBusinessType());
                            scheduleRecordService.save(scheduleRecord);
                        }
                    } else {
                        // 如果触发次数已经达到，则停止定时任务
                        log.info("触发次数到达上限，移除定时任务：taskId:{}", taskId);
                        CronUtil.remove(SCHEDULE_TASK_ID_PREFIX + taskId);

                        ScheduleTask update = new ScheduleTask();
                        update.setId(taskId);
                        update.setModifyTime(LocalDateTime.now());
                        update.setStatus(1);
                        scheduleTaskService.updateById(update);
                    }
                }
            } finally {
                MDC.clear();
            }
        };
    }

    @Override
    public String queueName() {
        return RedisKeys.SCHEDULE_TASK_CHANGE_QUEUE;
    }

    @Override
    public void consume(ScheduleTaskChange change) {
        AssertUtils.isNotNull(change, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(change.getId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(change.getChangeType(), ResultCodeEnum.PARAM_ERROR);

        if (Objects.equals(change.getChangeType(), ScheduleTaskChange.SAVE)) {
            AssertUtils.isNotBlank(change.getCron(), ResultCodeEnum.PARAM_ERROR);

            CronUtil.remove(SCHEDULE_TASK_ID_PREFIX + change.getId());
            CronUtil.schedule(SCHEDULE_TASK_ID_PREFIX + change.getId(), change.getCron(), executeSchedule(change.getId()));
            log.info("save cron task:{}", change.getId());
        } else {
            CronUtil.remove(SCHEDULE_TASK_ID_PREFIX + change.getId());
            log.info("remove cron task:{}", change.getId());
        }
    }
}
