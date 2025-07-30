package cn.fudges.server.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 *
 * @author wpy
 * @since 2025-07-24
 */
@Data
public class FileUploadResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 地址
     */
    private String filePath;

    /**
     * 名称
     */
    private String name;

    /**
     * 格式，后缀
     */
    private String suffix;

    /**
     * 大小，单位kb
     */
    private Integer size;
}
