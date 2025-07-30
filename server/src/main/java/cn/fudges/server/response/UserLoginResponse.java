package cn.fudges.server.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 王平远
 * @since 2025/5/24
 */
@Data
public class UserLoginResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 手机号
     */
    private String mobilePhone;

    private String imgUrl;

    private LocalDateTime birthday;

    private Integer sex;

    private String sign;

    private Integer passwordLength;

    private String token;
}
