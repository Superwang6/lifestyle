package cn.fudges.server.request;

import java.io.Serializable;
import java.time.LocalDateTime;

import cn.fudges.server.common.request.RequestEntity;
import cn.fudges.server.entity.bo.TimeCron;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* @author wpy
* @since 2025-05-24
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class JotRecordRequest extends RequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 分类id
     */
    private Long classifyId;

    /**
     * 备忘本id
     */
    private Long bookId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 状态，0-待处理，1-已处理，2-已忽略
     */
    private Integer status;

    /**
     * 提醒类型，0-单次，1-定时任务
     */
    private Integer remindType;

    /**
     * 提醒时间
     */
    private LocalDateTime remindTime;

    /**
     * 提醒状态，0-未提醒，1-已安排，2-推送成功，3-推送失败，4-提醒中
     */
    private Integer remindStatus;

    /**
     * 定时任务id
     */
    private Long scheduleTaskId;

    /**
     * 定时提醒json
     */
    private String remindTimeJson;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime modifyTime;

    private Integer delayDays;

    private Integer timeType;

    private TimeCron timeCron;
}
