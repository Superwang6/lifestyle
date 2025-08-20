package cn.fudges.server.business.jot.request;

import java.io.Serializable;

import cn.fudges.server.common.request.RequestEntity;
import lombok.Data;

/**
*
* @author wpy
* @since 2025-05-23
*/
@Data
public class JotBookRequest extends RequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 是否删除，0-否，1-是
     */
    private Byte isRemove;
}
