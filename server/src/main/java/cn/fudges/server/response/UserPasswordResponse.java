package cn.fudges.server.response;

import java.io.Serializable;

import lombok.Data;

/**
 *
 * @author wpy
 * @since 2025-05-24
 */
@Data
public class UserPasswordResponse implements Serializable {

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
}
