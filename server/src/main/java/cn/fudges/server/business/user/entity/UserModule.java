package cn.fudges.server.business.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户模块关系表
 * </p>
 *
 * @author wpy
 * @since 2025-08-12
 */
@Data
@TableName("user_module")
public class UserModule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 模块id
     */
    @TableField("module_id")
    private Integer moduleId;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private String iconUrl;

    @TableField(exist = false)
    private String webUrl;
}
