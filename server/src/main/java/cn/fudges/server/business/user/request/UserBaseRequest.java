package cn.fudges.server.business.user.request;

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
     * 头像图片地址
     */
    private String imgUrl;

    /**
     * 生日
     */
    private LocalDateTime birthday;

    /**
     * 性别，0-未知，1-男，2-女
     */
    private Byte sex;

    /**
     * 签名
     */
    private String sign;


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

    private String account;

    private String code;
}
