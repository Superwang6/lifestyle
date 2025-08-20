package cn.fudges.server.business.user.service;

import cn.fudges.server.business.user.entity.ModuleInfo;
import cn.fudges.server.business.user.request.ModuleInfoRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 功能模块 服务类
 * </p>
 *
 * @author wpy
 * @since 2025-08-12
 */
public interface ModuleInfoService extends IService<ModuleInfo> {

    List<ModuleInfo> queryModules(ModuleInfoRequest request);

    List<ModuleInfo> queryModuleByIdList(List<Integer> moduleIdList);
}
