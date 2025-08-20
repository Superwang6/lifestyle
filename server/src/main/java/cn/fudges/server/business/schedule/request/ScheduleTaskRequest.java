package cn.fudges.server.business.schedule.request;

import java.io.Serializable;
import java.time.LocalDateTime;

import cn.fudges.server.common.request.RequestEntity;
import cn.fudges.server.business.schedule.enums.ScheduleTaskBusinessType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* @author wpy
* @since 2025-07-02
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleTaskRequest extends RequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 提醒的cron表达式
     */
    private String cron;

    /**
     * 触发次数
     */
    private Integer triggerTimes;

    /**
     * 名称
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 状态，0-开启，1-关闭
     */
    private Integer status;

    private ScheduleTaskBusinessType businessType;
}
