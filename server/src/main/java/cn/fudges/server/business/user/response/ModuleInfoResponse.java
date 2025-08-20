package cn.fudges.server.business.user.response;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 *
 * @author wpy
 * @since 2025-08-12
 */
@Data
public class ModuleInfoResponse implements Serializable {

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

    private Boolean checked;
}
