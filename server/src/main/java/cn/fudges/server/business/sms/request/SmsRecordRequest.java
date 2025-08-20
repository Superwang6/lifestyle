package cn.fudges.server.business.sms.request;

import java.io.Serializable;
import java.time.LocalDateTime;

import cn.fudges.server.common.request.RequestEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* @author wpy
* @since 2025-08-06
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class SmsRecordRequest extends RequestEntity implements Serializable {

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

    /**
     * 业务类型
     */
    private String business;
}
