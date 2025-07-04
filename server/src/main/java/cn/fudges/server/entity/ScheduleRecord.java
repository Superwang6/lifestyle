package cn.fudges.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 定时任务触发记录
 * </p>
 *
 * @author wpy
 * @since 2025-07-02
 */
@Data
@TableName("schedule_record")
public class ScheduleRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 任务id
     */
    @TableField("schedule_task_id")
    private Long scheduleTaskId;

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
     * 业务类型，0-备忘录
     */
    @TableField("business_type")
    private Integer businessType;

    /**
     * 结果，0-成功，1-失败
     */
    @TableField("result")
    private Integer result;
}
