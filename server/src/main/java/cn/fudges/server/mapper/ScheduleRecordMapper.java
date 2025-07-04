package cn.fudges.server.mapper;

import cn.fudges.server.entity.ScheduleRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 定时任务触发记录 Mapper 接口
 * </p>
 *
 * @author wpy
 * @since 2025-07-02
 */
public interface ScheduleRecordMapper extends BaseMapper<ScheduleRecord> {

    List<Map<String, Object>> queryTriggerTimesByTaskIdList(List<Long> taskIdList);
}
