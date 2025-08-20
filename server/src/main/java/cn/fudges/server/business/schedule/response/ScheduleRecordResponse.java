package cn.fudges.server.business.schedule.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 *
 * @author wpy
 * @since 2025-07-02
 */
@Data
public class ScheduleRecordResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 任务id
     */
    private Long scheduleTaskId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 用户id
     */
    private Long userId;
}
