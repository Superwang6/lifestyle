package cn.fudges.server.service;

import cn.fudges.server.entity.UserBase;
import cn.fudges.server.request.UserPasswordRegisterRequest;

/**
 * @author 王平远
 * @since 2025/7/23
 */

public interface UserRegisterService {
    UserBase registerUserPassword(UserPasswordRegisterRequest request);
}
