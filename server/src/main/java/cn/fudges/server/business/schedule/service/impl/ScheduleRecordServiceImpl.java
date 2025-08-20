package cn.fudges.server.business.schedule.service.impl;

import cn.fudges.server.business.schedule.entity.ScheduleRecord;
import cn.fudges.server.business.schedule.mapper.ScheduleRecordMapper;
import cn.fudges.server.business.schedule.service.ScheduleRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务触发记录 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2025-07-02
 */
@Service
@RequiredArgsConstructor
public class ScheduleRecordServiceImpl extends ServiceImpl<ScheduleRecordMapper, ScheduleRecord> implements ScheduleRecordService {

    private final ScheduleRecordMapper scheduleRecordMapper;

    @Override
    public void removeByTaskId(Long scheduleTaskId) {
        LambdaQueryWrapper<ScheduleRecord> queryWrapper = new LambdaQueryWrapper<>();
        remove(queryWrapper.eq(ScheduleRecord::getScheduleTaskId, scheduleTaskId));
    }

    @Override
    public Integer queryTriggerTimeByTaskId(Long taskId) {
        return scheduleRecordMapper.queryTriggerTimeByTaskId(taskId);
    }
}
