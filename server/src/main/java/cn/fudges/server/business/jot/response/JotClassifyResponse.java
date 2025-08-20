package cn.fudges.server.business.jot.response;

import java.io.Serializable;

import lombok.Data;

/**
 *
 * @author wpy
 * @since 2025-05-24
 */
@Data
public class JotClassifyResponse implements Serializable {

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
