package cn.fudges.server.service;

import cn.fudges.server.entity.UserBase;
import cn.fudges.server.request.UserBaseRequest;
import cn.fudges.server.request.UserPasswordRequest;
import cn.fudges.server.response.UserBaseResponse;
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

    UserBase detail(Integer id);
}
