package cn.fudges.server.business.sms.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 *
 * @author wpy
 * @since 2025-08-06
 */
@Data
public class SmsRecordResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 目标手机号
     */
    private String mobilePhone;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 签名
     */
    private String sign;

    /**
     * 发送人id
     */
    private Long sourceUserId;

    /**
     * 发送人名称
     */
    private String sourceUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
