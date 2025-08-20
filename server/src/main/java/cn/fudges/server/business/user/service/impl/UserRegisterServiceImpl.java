package cn.fudges.server.business.user.service.impl;

import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.business.user.entity.UserBase;
import cn.fudges.server.business.user.entity.UserPassword;
import cn.fudges.server.business.user.request.UserMobileRegisterRequest;
import cn.fudges.server.business.user.request.UserPasswordRegisterRequest;
import cn.fudges.server.business.user.service.UserRegisterService;
import cn.fudges.server.business.user.service.UserBaseService;
import cn.fudges.server.business.user.service.UserPasswordService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 王平远
 * @since 2025/7/23
 */
@Service
@RequiredArgsConstructor
public class UserRegisterServiceImpl implements UserRegisterService {

    private final UserBaseService userBaseService;

    private final UserPasswordService userPasswordService;

    @Transactional
    @Override
    public UserBase registerUserPassword(UserPasswordRegisterRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getAccount(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getPassword(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getLength(), ResultCodeEnum.PARAM_ERROR);

        LambdaQueryWrapper<UserBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserBase::getAccount, request.getAccount());
        UserBase userBase = userBaseService.getOne(wrapper);
        AssertUtils.isNull(userBase, ResultCodeEnum.PARAM_ERROR, "账号已经存在");

        UserBase user = BeanUtil.copyProperties(request, UserBase.class);
        userBaseService.saveUser(user);

        UserPassword userPassword = BeanUtil.copyProperties(request, UserPassword.class);
        userPassword.setUserId(user.getId());
        userPassword.setSalt(RandomUtil.randomString(10));
        userPassword.setPassword(DigestUtil.md5Hex(userPassword.getPassword() + userPassword.getSalt()));
        userPasswordService.save(userPassword);
        return user;
    }

    @Override
    public UserBase registerUserMobilePhone(UserMobileRegisterRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getMobilePhone(), ResultCodeEnum.PARAM_ERROR);

        LambdaQueryWrapper<UserBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserBase::getMobilePhone, request.getMobilePhone());
        UserBase userBase = userBaseService.getOne(wrapper);
        AssertUtils.isNull(userBase, ResultCodeEnum.PARAM_ERROR, "手机号码已经存在");

        UserBase user = BeanUtil.copyProperties(request, UserBase.class);
        userBaseService.saveUser(user);
        return user;
    }
}
