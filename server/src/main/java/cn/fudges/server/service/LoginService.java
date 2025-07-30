package cn.fudges.server.service;

import cn.fudges.server.request.UserPasswordRequest;
import cn.fudges.server.response.UserLoginResponse;

/**
 * @author 王平远
 * @since 2025/7/23
 */

public interface LoginService {
    UserLoginResponse loginByPassword(UserPasswordRequest request);
}
