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
 * 用户表
 * </p>
 *
 * @author wpy
 * @since 2025-06-26
 */
@Data
@TableName("user_base")
public class UserBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 头像图片地址
     */
    @TableField("img_url")
    private String imgUrl;

    /**
     * 生日
     */
    @TableField("birthday")
    private LocalDateTime birthday;

    /**
     * 性别，0-未知，1-男，2-女
     */
    @TableField("sex")
    private Byte sex;

    /**
     * 签名
     */
    @TableField("sign")
    private String sign;

    /**
     * 手机号
     */
    @TableField("mobile_phone")
    private String mobilePhone;

    /**
     * uni-push2的client id
     */
    @TableField("uni_push_cid")
    private String uniPushCid;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("modify_time")
    private LocalDateTime modifyTime;

    /**
     * 是否删除，0-否，1-是
     */
    @TableField("is_remove")
    private Integer isRemove;
}
