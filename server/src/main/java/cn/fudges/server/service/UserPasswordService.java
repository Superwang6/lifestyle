package cn.fudges.server.service;

import cn.fudges.server.entity.UserPassword;
import cn.fudges.server.request.UserPasswordRequest;
import cn.fudges.server.response.UserLoginResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户密码表 服务类
 * </p>
 *
 * @author wpy
 * @since 2025-05-24
 */
public interface UserPasswordService extends IService<UserPassword> {

    UserLoginResponse loginByPassword(UserPasswordRequest request);
}
