package cn.fudges.server.business.user.service.impl;

import cn.fudges.server.business.user.entity.ModuleInfo;
import cn.fudges.server.business.user.mapper.ModuleInfoMapper;
import cn.fudges.server.business.user.request.ModuleInfoRequest;
import cn.fudges.server.business.user.service.ModuleInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 功能模块 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2025-08-12
 */
@Service
public class ModuleInfoServiceImpl extends ServiceImpl<ModuleInfoMapper, ModuleInfo> implements ModuleInfoService {

    @Override
    public List<ModuleInfo> queryModules(ModuleInfoRequest request) {
        LambdaQueryWrapper<ModuleInfo> query = new LambdaQueryWrapper<>();
        if(ObjectUtil.isNotNull(request)) {
            if(StrUtil.isNotBlank(request.getName())) {
                query.like(ModuleInfo::getName, request.getName());
            }
        }
        query.eq(ModuleInfo::getHide, 0);
        query.orderByAsc(ModuleInfo::getSort);
        return list(query);
    }

    @Override
    public List<ModuleInfo> queryModuleByIdList(List<Integer> moduleIdList) {
        LambdaQueryWrapper<ModuleInfo> query = new LambdaQueryWrapper<>();
        query.in(ModuleInfo::getId, moduleIdList).eq(ModuleInfo::getHide, 0);
        return list(query);
    }
}
