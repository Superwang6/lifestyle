package cn.fudges.server.request;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import cn.fudges.server.common.request.RequestEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wpy
 * @since 2025-06-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PushRecordRequest extends RequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 目标人用户id
     */
    private Long targetUserId;

    /**
     * 发送人用户id
     */
    private Long sourceUserId;

    private String title;

    private String content;

    private Map<String, Object> payload;
}
