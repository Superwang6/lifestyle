package cn.fudges.server.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 *
 * @author wpy
 * @since 2025-07-02
 */
@Data
public class ScheduleTaskResponse implements Serializable {

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
}
