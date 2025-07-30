package cn.fudges.server.request;

import java.io.Serializable;

import cn.fudges.server.common.request.RequestEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* @author wpy
* @since 2025-05-24
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class UserPasswordRequest extends RequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 密码，加密
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 密码位数
     */
    private Integer length;

    /**
     * 用户id
     */
    private Long userId;

    private String mobilePhone;

    private String uniPushCid;

    private String oldPassword;
}
