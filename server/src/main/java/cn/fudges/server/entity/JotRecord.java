package cn.fudges.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 备忘记录
 * </p>
 *
 * @author wpy
 * @since 2025-05-24
 */
@Data
@TableName("jot_record")
public class JotRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 分类id
     */
    @TableField("classify_id")
    private Long classifyId;

    /**
     * 备忘本id
     */
    @TableField("book_id")
    private Long bookId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 状态，0-待处理，1-已处理，2-已忽略
     */
    @TableField("status")
    private Integer status;

    /**
     * 提醒时间
     */
    @TableField("remind_time")
    private LocalDateTime remindTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("modify_time")
    private LocalDateTime modifyTime;

    @TableField(exist = false)
    private String classifyName;
    
    @TableField(exist = false)
    private String bookName;
}
