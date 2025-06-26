package cn.fudges.server.request;

import java.io.Serializable;
import java.time.LocalDateTime;

import cn.fudges.server.common.request.RequestEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author wpy
 * @since 2025-06-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserBaseRequest extends RequestEntity implements Serializable {

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
     * uni-push2的client id
     */
    private String uniPushCid;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 是否删除，0-否，1-是
     */
    private Integer isRemove;
}
