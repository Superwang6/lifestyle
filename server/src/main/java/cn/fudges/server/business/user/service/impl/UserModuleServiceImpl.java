package cn.fudges.server.business.user.service.impl;

import cn.fudges.server.business.user.entity.ModuleInfo;
import cn.fudges.server.business.user.entity.UserModule;
import cn.fudges.server.business.user.mapper.UserModuleMapper;
import cn.fudges.server.business.user.request.ModuleInfoRequest;
import cn.fudges.server.business.user.request.SaveUserModuleRequest;
import cn.fudges.server.business.user.request.UserModuleRequest;
import cn.fudges.server.business.user.service.ModuleInfoService;
import cn.fudges.server.business.user.service.UserModuleService;
import cn.fudges.server.common.RedisKeys;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.utils.AssertUtils;
import cn.fudges.server.utils.queue.QueueUtils;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户模块关系表 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2025-08-12
 */
@Service
@RequiredArgsConstructor
public class UserModuleServiceImpl extends ServiceImpl<UserModuleMapper, UserModule> implements UserModuleService {

    private final UserModuleMapper userModuleMapper;

    private final ModuleInfoService moduleInfoService;

    private final QueueUtils queueUtils;

    @Override
    public List<UserModule> queryUserModuleList(Long userId) {
        AssertUtils.isNotNull(userId, ResultCodeEnum.PARAM_ERROR);
        return userModuleMapper.queryUserModuleList(userId);
    }

    @Transactional
    @Override
    public Boolean saveUserModule(SaveUserModuleRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getUserId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getModuleId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getChecked(), ResultCodeEnum.PARAM_ERROR);

        if(request.getChecked()) {
            ModuleInfo info = moduleInfoService.getById(request.getModuleId());
            AssertUtils.isNotNull(info, ResultCodeEnum.PARAM_ERROR);

            UserModule userModule = new UserModule();
            userModule.setUserId(request.getUserId());
            userModule.setModuleId(request.getModuleId());
            userModule.setSort(info.getSort());
            save(userModule);

            queueUtils.sendMessage(RedisKeys.USER_MODULE_UPDATE_MODULE_QUEUE, userModule);
        } else {
            LambdaQueryWrapper<UserModule> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserModule::getModuleId, request.getModuleId()).eq(UserModule::getUserId, request.getUserId());
            List<UserModule> list = list(queryWrapper);
            if(CollUtil.isNotEmpty(list)) {
                List<Long> userModuleIdList = list.stream().map(UserModule::getId).toList();
                removeBatchByIds(userModuleIdList);
            }
        }
        return true;
    }

    @Override
    public List<UserModule> queryOwnModules(UserModuleRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getUserId(), ResultCodeEnum.PARAM_ERROR);

        LambdaQueryWrapper<UserModule> query = new LambdaQueryWrapper<>();
        query.eq(UserModule::getUserId, request.getUserId()).orderByAsc(UserModule::getSort);
        List<UserModule> list = list(query);
        List<Integer> moduleIdList = list.stream().map(UserModule::getModuleId).toList();
        if(CollUtil.isNotEmpty(moduleIdList)) {
            List<ModuleInfo> moduleInfoList = moduleInfoService.queryModuleByIdList(moduleIdList);
            Map<Integer, ModuleInfo> moduleInfoMap = moduleInfoList.stream().collect(Collectors.toMap(ModuleInfo::getId, Function.identity()));

            for (UserModule userModule : list) {
                if(moduleInfoMap.containsKey(userModule.getModuleId())) {
                    ModuleInfo moduleInfo = moduleInfoMap.get(userModule.getModuleId());
                    userModule.setName(moduleInfo.getName());
                    userModule.setIconUrl(moduleInfo.getIconUrl());
                    userModule.setWebUrl(moduleInfo.getWebUrl());
                }
            }
        }
        return list;
    }

    @Override
    public List<ModuleInfo> queryUserSettingModules(UserModuleRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getUserId(), ResultCodeEnum.PARAM_ERROR);

        LambdaQueryWrapper<UserModule> query = new LambdaQueryWrapper<>();
        query.eq(UserModule::getUserId, request.getUserId()).orderByAsc(UserModule::getSort);
        List<UserModule> list = list(query);
        Map<Integer, UserModule> userModuleMap = list.stream().collect(Collectors.toMap(UserModule::getModuleId, Function.identity()));

        List<ModuleInfo> moduleInfoList = moduleInfoService.queryModules(null);

        for (ModuleInfo moduleInfo : moduleInfoList) {
            if(userModuleMap.containsKey(moduleInfo.getId())) {
                moduleInfo.setChecked(true);
            } else {
                moduleInfo.setChecked(false);
            }
        }
        return moduleInfoList;
    }

}
