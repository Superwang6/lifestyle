package cn.fudges.server.response;

import java.io.Serializable;

import lombok.Data;

/**
 * @author wpy
 * @since 2025-05-23
 */
@Data
public class UserBaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 是否删除，0-否，1-是
     */
    private Integer isRemove;
}
