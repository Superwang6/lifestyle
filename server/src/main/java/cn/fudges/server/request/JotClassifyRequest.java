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
public class JotClassifyRequest extends RequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 记录本id
     */
    private Long bookId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 是否删除，0-否，1-是
     */
    private Byte isRemove;
}
