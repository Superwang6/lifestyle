package cn.fudges.server.business.user.request;

import cn.fudges.server.common.request.RequestEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 王平远
 * @since 2025/8/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SaveUserModuleRequest extends RequestEntity {

    private Long userId;

    private Integer moduleId;

    private Boolean checked;
}
