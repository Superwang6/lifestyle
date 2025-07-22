package cn.fudges.server.service.impl;

import cn.fudges.server.entity.ScheduleRecord;
import cn.fudges.server.mapper.ScheduleRecordMapper;
import cn.fudges.server.request.ScheduleRecordRequest;
import cn.fudges.server.service.ScheduleRecordService;
import cn.fudges.server.service.processor.ScheduleTaskProcessor;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.cron.task.Task;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    public Map<Long, Integer> queryTriggerTimesByTaskIdList(List<Long> taskIdList) {
        List<Map<String, Object>> resultList = scheduleRecordMapper.queryTriggerTimesByTaskIdList(taskIdList);
        Map<Long, Integer> result = MapUtil.newHashMap();
        for (Map<String, Object> map : resultList) {
            Long taskId = MapUtil.getLong(map, "taskId");
            Integer recordCount = MapUtil.getInt(map, "recordCount");
            result.put(taskId, recordCount);
        }
        return result;
    }
}
