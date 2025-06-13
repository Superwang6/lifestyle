package cn.fudges.server.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 *
 * @author wpy
 * @since 2025-05-23
 */
@Data
public class JotBookResponse implements Serializable {

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

    private List<JotClassifyResponse> classifyList;
}
