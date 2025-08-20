package cn.fudges.server.business.user.service;

import cn.fudges.server.business.user.entity.UserPassword;
import cn.fudges.server.business.user.request.UserPasswordRequest;
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

    Boolean modifyUserPassword(UserPasswordRequest request);

    Boolean forgetPassword(UserPasswordRequest request);
}
