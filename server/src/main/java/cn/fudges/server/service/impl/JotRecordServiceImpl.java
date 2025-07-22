package cn.fudges.server.service.impl;

import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.entity.JotRecord;
import cn.fudges.server.enums.JotRemindTypeEnum;
import cn.fudges.server.enums.ScheduleTaskBusinessType;
import cn.fudges.server.mapper.JotRecordMapper;
import cn.fudges.server.request.JotRecordRequest;
import cn.fudges.server.request.ScheduleTaskRequest;
import cn.fudges.server.service.event.JotRemindEvent;
import cn.fudges.server.service.JotRecordService;
import cn.fudges.server.service.ScheduleTaskService;
import cn.fudges.server.service.processor.ScheduleTaskProcessor;
import cn.fudges.server.utils.AssertUtils;
import cn.fudges.server.utils.MybatisPlusUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 备忘记录 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
@Service
@RequiredArgsConstructor
public class JotRecordServiceImpl extends ServiceImpl<JotRecordMapper, JotRecord> implements JotRecordService, ScheduleTaskProcessor {

    private final ApplicationContext applicationContext;

    private final JotRecordMapper jotRecordMapper;

    private final ScheduleTaskService scheduleTaskService;


    @Override
    public IPage<JotRecord> queryPage(JotRecordRequest request) {
        if (request.getTimeType() != null) {
            switch (request.getTimeType()) {
                case 3:
                    request.setRemindType(JotRemindTypeEnum.ONCE.getCode());
                    request.setStartTime(LocalDateTime.now());
                    break;
                case 4:
                    request.setRemindType(JotRemindTypeEnum.ONCE.getCode());
                    request.setEndTime(LocalDateTime.now());
                    break;
                case 5:
                    request.setRemindType(JotRemindTypeEnum.CRON.getCode());
                    break;
            }
        }
        return jotRecordMapper.queryPageList(request.getPage(), request);
    }

    @Transactional
    @Override
    public Boolean addJotRecord(JotRecordRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getTitle(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getBookId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getStatus(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getUserId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getRemindType(), ResultCodeEnum.PARAM_ERROR);

        JotRecord jotRecord = BeanUtil.copyProperties(request, JotRecord.class);
        jotRecord.setCreateTime(LocalDateTime.now());
        jotRecord.setModifyTime(LocalDateTime.now());
        jotRecord.setRemindTimeJson(JSONUtil.toJsonStr(Dict.create().set("cron", request.getCronExpression()).set("triggerTimes", request.getTriggerTimes())));
        if (jotRecord.getRemindType() == JotRemindTypeEnum.CRON.getCode()) {
            ScheduleTaskRequest scheduleRequest = new ScheduleTaskRequest();
            scheduleRequest.setName(jotRecord.getTitle());
            scheduleRequest.setCron(request.getCronExpression());
            scheduleRequest.setTriggerTimes(request.getTriggerTimes());
            scheduleRequest.setUserId(request.getUserId());
            scheduleRequest.setBusinessType(getScheduleTaskBusinessType());
            Long scheduleTaskId = scheduleTaskService.saveScheduleTask(scheduleRequest);

            jotRecord.setScheduleTaskId(scheduleTaskId);
        }
        return save(jotRecord);
    }

    @Transactional
    @Override
    public Boolean modifyJotRecord(JotRecordRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getTitle(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getBookId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getStatus(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getUserId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getRemindType(), ResultCodeEnum.PARAM_ERROR);

        JotRecord record = getById(request.getId());
        AssertUtils.isNotNull(record, ResultCodeEnum.BUSINESS_EXCEPTION);
        AssertUtils.isTrue(record.getUserId().equals(request.getUserId()), ResultCodeEnum.PERMISSION_DENIED);

        JotRecord jotRecord = BeanUtil.copyProperties(request, JotRecord.class);
        jotRecord.setModifyTime(LocalDateTime.now());
        jotRecord.setRemindTimeJson(JSONUtil.toJsonStr(Dict.create().set("cron", request.getCronExpression()).set("triggerTimes", request.getTriggerTimes())));
        UpdateWrapper<JotRecord> updateWrapper = MybatisPlusUtils.buildUpdateWrapper(jotRecord, "id");
        if(ObjectUtil.isNotNull(jotRecord.getRemindType()) && jotRecord.getRemindType() == JotRemindTypeEnum.CRON.getCode()) {
            ScheduleTaskRequest scheduleRequest = new ScheduleTaskRequest();
            scheduleRequest.setId(record.getScheduleTaskId());
            scheduleRequest.setName(jotRecord.getTitle());
            scheduleRequest.setCron(request.getCronExpression());
            scheduleRequest.setTriggerTimes(request.getTriggerTimes());
            scheduleRequest.setUserId(jotRecord.getUserId());
            scheduleRequest.setBusinessType(getScheduleTaskBusinessType());
            Long scheduleTaskId = scheduleTaskService.saveScheduleTask(scheduleRequest);

            updateWrapper.set("schedule_task_id", scheduleTaskId);
            updateWrapper.set("remind_time", null);
        } else if(ObjectUtil.isNotNull(jotRecord.getRemindType()) && jotRecord.getRemindType() == JotRemindTypeEnum.ONCE.getCode()
                && record.getRemindType() == JotRemindTypeEnum.CRON.getCode() && ObjectUtil.isNotNull(record.getScheduleTaskId())) {
            // 从表达式 转为 按时间提醒，移除表达式
            scheduleTaskService.removeScheduleTask(record.getScheduleTaskId());

            updateWrapper.set("schedule_task_id", null);
        }

        return update(updateWrapper);
    }

    @Transactional
    @Override
    public Boolean delete(JotRecordRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getUserId(), ResultCodeEnum.PARAM_ERROR);

        JotRecord record = getById(request.getId());
        AssertUtils.isNotNull(record, ResultCodeEnum.BUSINESS_EXCEPTION);
        AssertUtils.isTrue(record.getUserId().equals(request.getUserId()), ResultCodeEnum.PERMISSION_DENIED);

        if(ObjectUtil.isNotNull(record.getRemindType()) && record.getRemindType() == JotRemindTypeEnum.CRON.getCode() && ObjectUtil.isNotNull(record.getScheduleTaskId())) {
            scheduleTaskService.removeScheduleTask(record.getScheduleTaskId());
        }
        return removeById(request.getId());
    }

    @Override
    public Boolean delay(JotRecordRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getDelayDays(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getUserId(), ResultCodeEnum.PARAM_ERROR);

        JotRecord record = getById(request.getId());
        AssertUtils.isNotNull(record, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isTrue(record.getRemindType().equals(JotRemindTypeEnum.ONCE.getCode()), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isTrue(record.getUserId().equals(request.getUserId()), ResultCodeEnum.PERMISSION_DENIED);

        LocalDateTime newRemindTime = record.getRemindTime().plusDays(request.getDelayDays());
        JotRecord modifyRecord = new JotRecord();
        modifyRecord.setId(request.getId());
        modifyRecord.setRemindTime(newRemindTime);
        modifyRecord.setModifyTime(LocalDateTime.now());
        return updateById(modifyRecord);
    }

    @Override
    public void jotRemindJob() {
        LambdaQueryWrapper<JotRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JotRecord::getRemindStatus, 0).le(JotRecord::getRemindTime, LocalDateTime.now());
        List<JotRecord> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            for (JotRecord record : list) {
                remindJot(record, true);
            }
        }
    }

    @Override
    public Boolean modifyStatus(JotRecordRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getStatus(), ResultCodeEnum.PARAM_ERROR);

        JotRecord record = getById(request.getId());
        AssertUtils.isNotNull(record, ResultCodeEnum.BUSINESS_EXCEPTION);
        AssertUtils.isTrue(record.getUserId().equals(request.getUserId()), ResultCodeEnum.PERMISSION_DENIED);

        if(!ObjectUtil.equal(0, request.getRemindStatus()) && record.getRemindType() == JotRemindTypeEnum.CRON.getCode() && ObjectUtil.isNotNull(record.getScheduleTaskId())) {
            scheduleTaskService.removeScheduleTask(record.getScheduleTaskId());
        }

        JotRecord jotRecord = BeanUtil.copyProperties(request, JotRecord.class);
        jotRecord.setModifyTime(LocalDateTime.now());
        return updateById(jotRecord);
    }

    private void remindJot(JotRecord record, Boolean timesExhaustion) {
        AssertUtils.isNotNull(record, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(record.getId(), ResultCodeEnum.PARAM_ERROR);

        JotRecord update = new JotRecord();
        update.setId(record.getId());
        update.setRemindStatus(1);
        update.setModifyTime(LocalDateTime.now());
        updateById(update);

        JotRemindEvent event = new JotRemindEvent(this);
        event.setJotRecordId(record.getId());
        event.setTimesExhaustion(timesExhaustion);
        applicationContext.publishEvent(event);
    }

    @Override
    public ScheduleTaskBusinessType getScheduleTaskBusinessType() {
        return ScheduleTaskBusinessType.JOT_REMIND;
    }

    @Override
    public void scheduleTaskExecute(Long taskId, Map<String,Object> extendFields) {
        Boolean timesExhaustion = MapUtil.getBool(extendFields, "timesExhaustion", false);
        LambdaQueryWrapper<JotRecord> lambdaWrapper = new LambdaQueryWrapper<>();
        lambdaWrapper.eq(JotRecord::getScheduleTaskId, taskId);
        List<JotRecord> list = list(lambdaWrapper);
        if(CollUtil.isNotEmpty(list)) {
            for (JotRecord record : list) {
                remindJot(record, timesExhaustion);
            }
        }
    }
}
