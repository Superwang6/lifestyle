package cn.fudges.server.business.schedule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 定时任务
 * </p>
 *
 * @author wpy
 * @since 2025-07-03
 */
@Data
@TableName("schedule_task")
public class ScheduleTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 提醒的cron表达式
     */
    @TableField("cron")
    private String cron;

    /**
     * 触发次数
     */
    @TableField("trigger_times")
    private Integer triggerTimes;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 修改时间
     */
    @TableField("modify_time")
    private LocalDateTime modifyTime;

    /**
     * 业务类型，0-备忘录
     */
    @TableField("business_type")
    private Integer businessType;

    /**
     * 状态，0-开启，1-关闭
     */
    @TableField("status")
    private Integer status;
}
