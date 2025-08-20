package cn.fudges.server.business.user.service;

import cn.fudges.server.business.user.entity.ModuleInfo;
import cn.fudges.server.business.user.entity.UserModule;
import cn.fudges.server.business.user.request.SaveUserModuleRequest;
import cn.fudges.server.business.user.request.UserModuleRequest;
import cn.fudges.server.business.user.response.ModuleInfoResponse;
import cn.fudges.server.business.user.response.UserModuleResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户模块关系表 服务类
 * </p>
 *
 * @author wpy
 * @since 2025-08-12
 */
public interface UserModuleService extends IService<UserModule> {

    List<UserModule> queryUserModuleList(Long userId);

    Boolean saveUserModule(SaveUserModuleRequest request);

    List<UserModule> queryOwnModules(UserModuleRequest request);

    List<ModuleInfo> queryUserSettingModules(UserModuleRequest request);
}
