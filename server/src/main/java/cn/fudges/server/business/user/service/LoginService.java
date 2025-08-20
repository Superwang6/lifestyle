package cn.fudges.server.business.user.service;

import cn.fudges.server.business.user.entity.bo.UserLogin;
import cn.fudges.server.business.user.request.UserPasswordRequest;
import cn.fudges.server.business.user.request.UserSmsRequest;
import cn.fudges.server.business.user.response.UserLoginResponse;

/**
 * @author 王平远
 * @since 2025/7/23
 */

public interface LoginService {
    UserLogin loginByPassword(UserPasswordRequest request);

    UserLogin loginByMobilePhone(UserSmsRequest request);
}
