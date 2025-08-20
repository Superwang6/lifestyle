package cn.fudges.server.business.user.request;

import cn.fudges.server.common.request.RequestEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 王平远
 * @since 2025/8/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserSmsRequest extends RequestEntity {

    private String mobilePhone;

    private String code;

    private String uniPushCid;
}
