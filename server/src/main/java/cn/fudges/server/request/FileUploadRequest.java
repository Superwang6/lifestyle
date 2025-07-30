package cn.fudges.server.request;

import cn.fudges.server.common.request.RequestEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* @author wpy
* @since 2025-07-24
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class FileUploadRequest extends RequestEntity {

    private String businessName;

    private Long userId;

    private String[] base64s;

    private String base64;
}
