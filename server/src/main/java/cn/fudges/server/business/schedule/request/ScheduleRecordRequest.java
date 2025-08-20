package cn.fudges.server.business.schedule.request;

import java.io.Serializable;
import java.time.LocalDateTime;

import cn.fudges.server.common.request.RequestEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* @author wpy
* @since 2025-07-02
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleRecordRequest extends RequestEntity implements Serializable {

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
