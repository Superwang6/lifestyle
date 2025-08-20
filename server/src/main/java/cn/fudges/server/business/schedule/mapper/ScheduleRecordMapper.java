package cn.fudges.server.business.schedule.mapper;

import cn.fudges.server.business.schedule.entity.ScheduleRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 定时任务触发记录 Mapper 接口
 * </p>
 *
 * @author wpy
 * @since 2025-07-02
 */
public interface ScheduleRecordMapper extends BaseMapper<ScheduleRecord> {

    Integer queryTriggerTimeByTaskId(Long taskId);
}
