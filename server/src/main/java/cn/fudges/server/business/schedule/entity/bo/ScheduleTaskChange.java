package cn.fudges.server.business.schedule.entity.bo;

import lombok.Data;

/**
 * @author 王平远
 * @since 2025/8/6
 */
@Data
public class ScheduleTaskChange {

    public static final Integer SAVE = 0;

    public static final Integer DELETE = 1;

    private Long id;

    private String cron;

    private Integer changeType;
}
