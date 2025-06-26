package cn.fudges.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 推送记录
 * </p>
 *
 * @author wpy
 * @since 2025-06-26
 */
@Data
@TableName("push_record")
public class PushRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 推送地址
     */
    @TableField("url")
    private String url;

    /**
     * 请求信息
     */
    @TableField("request")
    private String request;

    /**
     * 返回值
     */
    @TableField("response")
    private String response;

    /**
     * 返回码
     */
    @TableField("result_code")
    private Integer resultCode;

    /**
     * 目标id
     */
    @TableField("target_id")
    private String targetId;

    /**
     * 目标类型，0:uni-push-cid
     */
    @TableField("target_type")
    private Integer targetType;

    /**
     * 目标人用户id
     */
    @TableField("target_user_id")
    private Long targetUserId;

    /**
     * 发送人用户id
     */
    @TableField("source_user_id")
    private Long sourceUserId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
