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
public class UserModuleResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 模块id
     */
    private Integer moduleId;

    /**
     * 排序
     */
    private Integer sort;

    private String name;

    private String iconUrl;

    private String webUrl;
}
