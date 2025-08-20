package cn.fudges.server.business.user.service;

import cn.fudges.server.business.user.entity.UserBase;
import cn.fudges.server.business.user.request.UserMobileRegisterRequest;
import cn.fudges.server.business.user.request.UserPasswordRegisterRequest;

/**
 * @author 王平远
 * @since 2025/7/23
 */

public interface UserRegisterService {
    UserBase registerUserPassword(UserPasswordRegisterRequest request);

    UserBase registerUserMobilePhone(UserMobileRegisterRequest mobileRegisterRequest);
}
