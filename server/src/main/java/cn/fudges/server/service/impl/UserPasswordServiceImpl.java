package cn.fudges.server.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.entity.UserBase;
import cn.fudges.server.entity.UserPassword;
import cn.fudges.server.mapper.UserPasswordMapper;
import cn.fudges.server.request.UserPasswordRequest;
import cn.fudges.server.response.UserLoginResponse;
import cn.fudges.server.service.UserBaseService;
import cn.fudges.server.service.UserPasswordService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    private final UserBaseService userBaseService;

    @Override
    public UserLoginResponse loginByPassword(UserPasswordRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getMobilePhone(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getPassword(), ResultCodeEnum.PARAM_ERROR);

        LambdaQueryWrapper<UserBase> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(UserBase::getMobilePhone, request.getMobilePhone()).eq(UserBase::getIsRemove, 0);
        UserBase userBase = userBaseService.getOne(userQuery);
        AssertUtils.isNotNull(userBase, ResultCodeEnum.PARAM_ERROR, "用户名或密码错误！");

        LambdaQueryWrapper<UserPassword> passwordQuery = new LambdaQueryWrapper<>();
        passwordQuery.eq(UserPassword::getUserId, userBase.getId());
        UserPassword password = getOne(passwordQuery);
        AssertUtils.isNotNull(password, ResultCodeEnum.BUSINESS_EXCEPTION);

        String md5Password = DigestUtil.md5Hex(request.getPassword() + password.getSalt());
        AssertUtils.isTrue(md5Password.equals(password.getPassword()), ResultCodeEnum.ACCOUNT_PASSWORD_ERROR);

        // 登录成功
        StpUtil.login(userBase.getId());

        // 保存用户信息
        UserBase ub = new UserBase();
        ub.setId(userBase.getId());
        ub.setUniPushCid(request.getUniPushCid());
        ub.setModifyTime(LocalDateTime.now());
        userBaseService.updateById(ub);

        UserLoginResponse userLoginResponse = BeanUtil.copyProperties(userBase, UserLoginResponse.class);
        userLoginResponse.setPasswordLength(password.getLength());
        userLoginResponse.setToken(StpUtil.getTokenValue());
        return userLoginResponse;
    }
}
