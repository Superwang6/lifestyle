package cn.fudges.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 备忘本
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
@Data
@TableName("jot_book")
public class JotBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

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
