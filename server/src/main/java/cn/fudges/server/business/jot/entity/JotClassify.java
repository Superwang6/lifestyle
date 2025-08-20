package cn.fudges.server.business.jot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 备忘录分类
 * </p>
 *
 * @author wpy
 * @since 2025-05-24
 */
@Data
@TableName("jot_classify")
public class JotClassify implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 记录本id
     */
    @TableField("book_id")
    private Long bookId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 是否删除，0-否，1-是
     */
    @TableField("is_remove")
    private Byte isRemove;
}
