package cn.fudges.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户密码表
 * </p>
 *
 * @author wpy
 * @since 2025-05-24
 */
@Data
@TableName("user_password")
public class UserPassword implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 密码，加密
     */
    @TableField("password")
    private String password;

    /**
     * 盐
     */
    @TableField("salt")
    private String salt;

    /**
     * 密码位数
     */
    @TableField("length")
    private Integer length;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
}
