package cn.fudges.server.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.entity.ScheduleRecord;
import cn.fudges.server.entity.ScheduleTask;
import cn.fudges.server.mapper.ScheduleTaskMapper;
import cn.fudges.server.request.ScheduleTaskRequest;
import cn.fudges.server.service.ScheduleRecordService;
import cn.fudges.server.service.ScheduleTaskService;
import cn.fudges.server.service.processor.ScheduleTaskProcessor;
import cn.fudges.server.utils.AssertUtils;
import cn.fudges.server.utils.CronUtils;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class ScheduleTaskServiceImpl extends ServiceImpl<ScheduleTaskMapper, ScheduleTask> implements ScheduleTaskService, SmartInitializingSingleton {

    private final String SCHEDULE_TASK_ID_PREFIX = "schedule_task_";

    private final ScheduleRecordService scheduleRecordService;

    private final Map<Integer, ScheduleTaskProcessor> PROCESSOR_MAP = new HashMap<>();

    private final ApplicationContext applicationContext;

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
        List<ScheduleTask> list = list(queryWrapper);
        for (ScheduleTask scheduleTask : list) {
            CronUtil.remove(SCHEDULE_TASK_ID_PREFIX + scheduleTask.getId());
            CronUtil.schedule(SCHEDULE_TASK_ID_PREFIX + scheduleTask.getId(), scheduleTask.getCron(), executeSchedule(scheduleTask.getId()));
        }

        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

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
        if (ObjectUtil.isNotNull(request.getId())) {
            ScheduleTask oldTask = getById(request.getId());
            if(ObjectUtil.isNotNull(oldTask)) {
                AssertUtils.isTrue(oldTask.getUserId().equals(request.getUserId()), ResultCodeEnum.PERMISSION_DENIED);

                scheduleTask.setId(oldTask.getId());
                updateById(scheduleTask);
                CronUtil.remove(SCHEDULE_TASK_ID_PREFIX + oldTask.getId());
            } else {
                scheduleTask.setCreateTime(LocalDateTime.now());
                save(scheduleTask);
            }
        } else {
            scheduleTask.setCreateTime(LocalDateTime.now());
            save(scheduleTask);
        }
        CronUtil.schedule(SCHEDULE_TASK_ID_PREFIX + scheduleTask.getId(), request.getCron(), executeSchedule(scheduleTask.getId()));

        return scheduleTask.getId();
    }

    private Task executeSchedule(Long taskId) {
        AssertUtils.isNotNull(taskId, ResultCodeEnum.PARAM_ERROR);
        return () -> {
            try {
                MDC.put("traceId", IdUtil.fastSimpleUUID());
                log.info("执行定时任务：taskId:{}", taskId);
                ScheduleTask scheduleTask = getById(taskId);
                if(ObjectUtil.isNotNull(scheduleTask)) {
                    // 次数是否耗尽
                    boolean timesExhaustion = false;
                    if (ObjectUtil.isNotNull(scheduleTask.getTriggerTimes()) && scheduleTask.getTriggerTimes() > 0) {
                        Map<Long, Integer> timesMap = scheduleRecordService.queryTriggerTimesByTaskIdList(List.of(taskId));
                        if (ObjectUtil.isNotNull(timesMap) && ObjectUtil.isNotNull(timesMap.get(taskId))
                                && scheduleTask.getTriggerTimes() <= timesMap.get(taskId)) {
                            timesExhaustion = true;
                        }
                    }
                    if(!timesExhaustion) {
                        ScheduleRecord scheduleRecord = new ScheduleRecord();
                        try {
                            ScheduleTaskProcessor processor = PROCESSOR_MAP.get(scheduleTask.getBusinessType());
                            if(ObjectUtil.isNotNull(processor)) {
                                processor.scheduleTaskExecute(scheduleTask.getId(), Dict.create().set("timesExhaustion", timesExhaustion));
                                scheduleRecord.setResult(0);
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
                        updateById(update);
                    }
                } else {
                    log.info("任务id不存在，移除定时任务：taskId:{}", taskId);
                    CronUtil.remove(SCHEDULE_TASK_ID_PREFIX + taskId);
                }
            } finally {
                MDC.clear();
            }
        };
    }

    @Transactional
    @Override
    public void removeScheduleTask(Long scheduleTaskId) {
        AssertUtils.isNotNull(scheduleTaskId, ResultCodeEnum.PARAM_ERROR);

        ScheduleTask oldTask = getById(scheduleTaskId);
        AssertUtils.isTrue(oldTask.getUserId().equals(StpUtil.getLoginIdAsLong()), ResultCodeEnum.PERMISSION_DENIED);
        removeById(scheduleTaskId);

        scheduleRecordService.removeByTaskId(scheduleTaskId);

        boolean remove = CronUtil.remove(SCHEDULE_TASK_ID_PREFIX + scheduleTaskId);
        if(remove) {
            log.info("移除定时任务：taskId:{}", scheduleTaskId);
        }
    }
}
