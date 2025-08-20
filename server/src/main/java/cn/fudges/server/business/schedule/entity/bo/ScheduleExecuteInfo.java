package cn.fudges.server.business.schedule.entity.bo;

import lombok.Data;

/**
 * @author 王平远
 * @since 2025/8/6
 */
@Data
public class ScheduleExecuteInfo {

    private Long taskId;

    private Boolean timesExhaustion;
}
