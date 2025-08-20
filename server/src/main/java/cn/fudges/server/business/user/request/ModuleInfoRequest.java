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
public class ModuleInfoRequest extends RequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 解锁等级
     */
    private Integer unlockLevel;

    /**
     * 图标url
     */
    private String iconUrl;

    /**
     * 前端地址
     */
    private String webUrl;
}
