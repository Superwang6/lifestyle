package cn.fudges.server.service.impl;

import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.entity.UserPassword;
import cn.fudges.server.mapper.UserPasswordMapper;
import cn.fudges.server.request.UserPasswordRequest;
import cn.fudges.server.service.UserPasswordService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 用户密码表 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2025-05-24
 */
@Service
@RequiredArgsConstructor
public class UserPasswordServiceImpl extends ServiceImpl<UserPasswordMapper, UserPassword> implements UserPasswordService {

    @Override
    public Boolean modifyUserPassword(UserPasswordRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getUserId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getPassword(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getOldPassword(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getLength(), ResultCodeEnum.PARAM_ERROR);

        LambdaQueryWrapper<UserPassword> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPassword::getUserId, request.getUserId());
        UserPassword password = getOne(wrapper);
        AssertUtils.isNotNull(password, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isTrue(request.getOldPassword().equals(password.getPassword()), ResultCodeEnum.PARAM_ERROR, "旧密码不正确");

        UserPassword newPassword = new UserPassword();
        newPassword.setId(password.getId());
        newPassword.setUserId(password.getUserId());
        newPassword.setSalt(RandomUtil.randomString(10));
        newPassword.setLength(request.getLength());
        newPassword.setPassword(request.getPassword());
        return updateById(newPassword);
    }
}
