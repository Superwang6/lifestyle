package cn.fudges.server.business.user.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/**
 *
 * @author wpy
 * @since 2025-06-26
 */
@Data
public class UserBaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 账号
     */
    private String account;

    /**
     * 手机号
     */
    private String mobilePhone;

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

//    /**
//     * uni-push2的client id
//     */
//    private String uniPushCid;

    private List<UserModuleResponse> moduleList;
}
