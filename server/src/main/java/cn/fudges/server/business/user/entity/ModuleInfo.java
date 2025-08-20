package cn.fudges.server.business.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 功能模块
 * </p>
 *
 * @author wpy
 * @since 2025-08-18
 */
@Data
@TableName("module_info")
public class ModuleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 是否隐藏，0-否，1-隐藏
     */
    @TableField("is_hide")
    private Boolean hide;

    /**
     * 解锁等级
     */
    @TableField("unlock_level")
    private Integer unlockLevel;

    /**
     * 图标url
     */
    @TableField("icon_url")
    private String iconUrl;

    /**
     * 前端地址
     */
    @TableField("web_url")
    private String webUrl;

    @TableField(exist = false)
    private Boolean checked;
}
