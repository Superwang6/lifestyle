package cn.fudges.server.request;

import cn.fudges.server.common.request.RequestEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 王平远
 * @since 2025/7/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserPasswordRegisterRequest extends RequestEntity {

    private String mobilePhone;

    private String password;

    private Integer length;

    private String name;
}
