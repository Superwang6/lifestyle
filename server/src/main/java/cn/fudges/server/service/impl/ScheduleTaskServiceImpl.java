package cn.fudges.server.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.entity.ScheduleRecord;
import cn.fudges.server.entity.ScheduleTask;
import cn.fudges.server.mapper.ScheduleTaskMapper;
import cn.fudges.server.request.ScheduleRecordRequest;
import cn.fudges.server.request.ScheduleTaskRequest;
import cn.fudges.server.service.ScheduleRecordService;
import cn.fudges.server.service.ScheduleTaskService;
import cn.fudges.server.service.processor.ScheduleTaskProcessor;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        List<ScheduleTask> list = list();
        List<Long> triggerTimesIdList = new ArrayList<>();
        for (ScheduleTask scheduleTask : list) {
            if (ObjectUtil.isNotNull(scheduleTask.getTriggerTimes()) && scheduleTask.getTriggerTimes() > 0) {
                triggerTimesIdList.add(scheduleTask.getId());
            }
        }
        Map<Long, Integer> timesMap = null;
        if (CollUtil.isNotEmpty(triggerTimesIdList)) {
            timesMap = scheduleRecordService.queryTriggerTimesByTaskIdList(triggerTimesIdList);
        }
        for (ScheduleTask scheduleTask : list) {
            if (ObjectUtil.isNotNull(scheduleTask.getTriggerTimes()) && scheduleTask.getTriggerTimes() > 0
                    && ObjectUtil.isNotNull(timesMap) && timesMap.containsKey(scheduleTask.getId())
                    && scheduleTask.getTriggerTimes() >= timesMap.get(scheduleTask.getId())) {
                // 如果触发次数已经达到，则跳过不启动定时任务
                continue;
            }
            CronUtil.remove(SCHEDULE_TASK_ID_PREFIX + scheduleTask.getId());
            CronUtil.schedule(SCHEDULE_TASK_ID_PREFIX + scheduleTask.getId(), scheduleTask.getCron(), executeSchedule(scheduleTask.getId()));
        }

        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public Long saveScheduleTask(ScheduleTaskRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getTimeCron(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getName(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getUserId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getBusinessType(), ResultCodeEnum.PARAM_ERROR);

        String cron = request.getTimeCron().toCron();
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setCron(cron);
        scheduleTask.setName(request.getName());
        scheduleTask.setUserId(request.getUserId());
        scheduleTask.setModifyTime(LocalDateTime.now());
        scheduleTask.setBusinessType(request.getBusinessType().getCode());
        scheduleTask.setTriggerTimes(request.getTimeCron().getTriggerTimes());
        if (ObjectUtil.isNotNull(request.getId())) {
            ScheduleTask oldTask = getById(request.getId());
            AssertUtils.isTrue(oldTask.getUserId().equals(request.getUserId()), ResultCodeEnum.PERMISSION_DENIED);

            scheduleTask.setId(oldTask.getId());
            updateById(scheduleTask);
            CronUtil.remove(SCHEDULE_TASK_ID_PREFIX + oldTask.getId());
        } else {
            scheduleTask.setCreateTime(LocalDateTime.now());
            save(scheduleTask);
        }
        CronUtil.schedule(SCHEDULE_TASK_ID_PREFIX + scheduleTask.getId(), cron, executeSchedule(scheduleTask.getId()));

        return scheduleTask.getId();
    }

    private Task executeSchedule(Long taskId) {
        AssertUtils.isNotNull(taskId, ResultCodeEnum.PARAM_ERROR);
        return () -> {
            ScheduleTask scheduleTask = getById(taskId);
            ScheduleRecord scheduleRecord = new ScheduleRecord();
            // 次数是否耗尽
            boolean timesExhaustion = false;
            try {
                if (ObjectUtil.isNotNull(scheduleTask.getTriggerTimes()) && scheduleTask.getTriggerTimes() > 0) {
                    Map<Long, Integer> timesMap = scheduleRecordService.queryTriggerTimesByTaskIdList(List.of(taskId));
                    if (ObjectUtil.isNotNull(timesMap) && ObjectUtil.isNotNull(timesMap.get(taskId))
                            && scheduleTask.getTriggerTimes() < timesMap.get(taskId)) {
                        timesExhaustion = true;
                    }
                }
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


                // 如果触发次数已经达到，则停止定时任务
                if(timesExhaustion) {
                    log.info("触发次数到达上限，移除定时任务：taskId:{}", taskId);
                    CronUtil.remove(SCHEDULE_TASK_ID_PREFIX + taskId);
                }
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

        log.info("移除定时任务：taskId:{}", scheduleTaskId);
        CronUtil.remove(SCHEDULE_TASK_ID_PREFIX + scheduleTaskId);
    }
}
