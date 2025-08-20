package cn.fudges.server.business.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.business.user.entity.UserModule;
import cn.fudges.server.business.user.entity.bo.UserLogin;
import cn.fudges.server.business.user.service.*;
import cn.fudges.server.common.RedisKeys;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.business.user.entity.UserBase;
import cn.fudges.server.business.user.entity.UserPassword;
import cn.fudges.server.business.sms.enums.SmsBusinessEnum;
import cn.fudges.server.business.user.request.UserMobileRegisterRequest;
import cn.fudges.server.business.user.request.UserPasswordRequest;
import cn.fudges.server.business.user.request.UserSmsRequest;
import cn.fudges.server.business.user.response.UserLoginResponse;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 王平远
 * @since 2025/7/23
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserBaseService userBaseService;

    private final UserPasswordService userPasswordService;

    private final RedissonClient redissonClient;

    private final UserRegisterService userRegisterService;

    @Override
    public UserLogin loginByPassword(UserPasswordRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getAccount(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getPassword(), ResultCodeEnum.PARAM_ERROR);

        LambdaQueryWrapper<UserBase> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(UserBase::getAccount, request.getAccount()).eq(UserBase::getIsRemove, 0);
        UserBase userBase = userBaseService.getOne(userQuery);
        AssertUtils.isNotNull(userBase, ResultCodeEnum.PARAM_ERROR, "用户名或密码错误！");

        LambdaQueryWrapper<UserPassword> passwordQuery = new LambdaQueryWrapper<>();
        passwordQuery.eq(UserPassword::getUserId, userBase.getId());
        UserPassword password = userPasswordService.getOne(passwordQuery);
        AssertUtils.isNotNull(password, ResultCodeEnum.BUSINESS_EXCEPTION, "未设置密码，请通过验证码登录");

        String md5Password = DigestUtil.md5Hex(request.getPassword() + password.getSalt());
        AssertUtils.isTrue(md5Password.equals(password.getPassword()), ResultCodeEnum.ACCOUNT_PASSWORD_ERROR);

        userBase.setUniPushCid(request.getUniPushCid());
        UserLogin userLogin = loginSuccess(userBase);
        userLogin.setPasswordLength(password.getLength());
        return userLogin;
    }

    @Override
    public UserLogin loginByMobilePhone(UserSmsRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getMobilePhone(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isTrue(PhoneUtil.isMobile(request.getMobilePhone()), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getCode(), ResultCodeEnum.PARAM_ERROR);

        // 校验验证码
        RMapCache<String, String> smsCodeMapCache = redissonClient.getMapCache(RedisKeys.SMS_SEND_KEY + SmsBusinessEnum.loginCode.name());
        String code = smsCodeMapCache.get(request.getMobilePhone());
        AssertUtils.isTrue(request.getCode().equals(code), ResultCodeEnum.SMS_CODE_ERROR);
        smsCodeMapCache.remove(request.getMobilePhone());

        LambdaQueryWrapper<UserBase> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(UserBase::getMobilePhone, request.getMobilePhone()).eq(UserBase::getIsRemove, 0);
        UserBase userBase = userBaseService.getOne(userQuery);
        if(ObjectUtil.isNull(userBase)) {
            // 注册用户
            UserMobileRegisterRequest mobileRegisterRequest = new UserMobileRegisterRequest();
            mobileRegisterRequest.setMobilePhone(request.getMobilePhone());
            userBase = userRegisterService.registerUserMobilePhone(mobileRegisterRequest);
        }

        userBase.setUniPushCid(request.getUniPushCid());
        return loginSuccess(userBase);
    }

    private UserLogin loginSuccess(UserBase userBase) {
        // 登录成功
        StpUtil.login(userBase.getId());

        // 保存用户信息
        UserBase ub = new UserBase();
        ub.setId(userBase.getId());
        ub.setUniPushCid(userBase.getUniPushCid());
        ub.setModifyTime(LocalDateTime.now());
        userBaseService.updateById(ub);

        UserLogin userLogin = BeanUtil.copyProperties(userBase, UserLogin.class);
        userLogin.setToken(StpUtil.getTokenValue());
        return userLogin;
    }
}
