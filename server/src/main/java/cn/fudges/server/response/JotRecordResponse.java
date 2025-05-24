package cn.fudges.server.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 *
 * @author wpy
 * @since 2025-05-24
 */
@Data
public class JotRecordResponse implements Serializable {

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
     * 提醒时间
     */
    private LocalDateTime remindTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime modifyTime;
}
