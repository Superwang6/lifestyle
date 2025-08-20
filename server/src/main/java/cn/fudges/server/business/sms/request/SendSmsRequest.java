package cn.fudges.server.business.sms.request;

import cn.fudges.server.common.request.RequestEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 王平远
 * @since 2025/8/6
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class SendSmsRequest extends RequestEntity {

    private String mobilePhone;

    private String business;
}
