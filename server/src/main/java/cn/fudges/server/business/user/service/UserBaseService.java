package cn.fudges.server.business.user.service;

import cn.fudges.server.business.user.entity.UserBase;
import cn.fudges.server.business.user.request.UserBaseRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
public interface UserBaseService extends IService<UserBase> {

    UserBase detail(Long id);

    Boolean modifyUserBase(UserBaseRequest request);

    void saveUser(UserBase user);

    Boolean modifyAccount(UserBaseRequest request);

    Boolean modifyMobilePhone(UserBaseRequest request);

    String queryMobilePhone(Long id);
}
