package cn.fudges.server.service.impl;

import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.entity.UserBase;
import cn.fudges.server.mapper.UserBaseMapper;
import cn.fudges.server.request.UserBaseRequest;
import cn.fudges.server.service.UserBaseService;
import cn.fudges.server.utils.AssertUtils;
import cn.fudges.server.utils.FileUrlUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
@Service
@RequiredArgsConstructor
public class UserBaseServiceImpl extends ServiceImpl<UserBaseMapper, UserBase> implements UserBaseService {

    @Override
    public UserBase detail(Integer id) {
        return getById(id);
    }

    @Override
    public Boolean modifyUserBase(UserBaseRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getId(), ResultCodeEnum.PARAM_ERROR);

        UserBase userBase = new UserBase();
        userBase.setId(request.getId());
        userBase.setName(request.getName());
        userBase.setSex(request.getSex());
        userBase.setBirthday(request.getBirthday());
        userBase.setSign(request.getSign());
        userBase.setImgUrl(request.getImgUrl());
        userBase.setModifyTime(LocalDateTime.now());
        return updateById(userBase);
    }


}
