package cn.fudges.server.business.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 短信发送记录
 * </p>
 *
 * @author wpy
 * @since 2025-08-06
 */
@Data
@TableName("sms_record")
public class SmsRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 目标手机号
     */
    @TableField("mobile_phone")
    private String mobilePhone;

    /**
     * 消息内容
     */
    @TableField("message")
    private String message;

    /**
     * 签名
     */
    @TableField("sign")
    private String sign;

    /**
     * 发送人id
     */
    @TableField("source_user_id")
    private Long sourceUserId;

    /**
     * 发送人名称
     */
    @TableField("source_user_name")
    private String sourceUserName;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 业务类型
     */
    @TableField("business")
    private String business;
}
