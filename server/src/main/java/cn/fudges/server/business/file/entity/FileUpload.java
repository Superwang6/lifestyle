package cn.fudges.server.business.file.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wpy
 * @since 2025-07-24
 */
@Data
@TableName("file_upload")
public class FileUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 地址
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 格式，后缀
     */
    @TableField("suffix")
    private String suffix;

    /**
     * 大小，单位kb
     */
    @TableField("size")
    private Integer size;

    /**
     * 业务名称
     */
    @TableField("business_name")
    private String businessName;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    @TableField(exist = false)
    private byte[] base64;
}
