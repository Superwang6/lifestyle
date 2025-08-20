package cn.fudges.server.business.user.request;

import java.io.Serializable;

import cn.fudges.server.common.request.RequestEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* @author wpy
* @since 2025-08-12
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class UserModuleRequest extends RequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 模块id
     */
    private Integer moduleId;

    /**
     * 排序
     */
    private Integer sort;
}
