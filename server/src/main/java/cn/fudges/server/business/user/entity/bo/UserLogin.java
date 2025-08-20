package cn.fudges.server.business.user.entity.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 王平远
 * @since 2025/8/12
 */
@Data
public class UserLogin {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 手机号
     */
    private String mobilePhone;

    private String account;

    private String imgUrl;

    private LocalDateTime birthday;

    private Integer sex;

    private String sign;

    private Integer passwordLength;

    private String token;
}
