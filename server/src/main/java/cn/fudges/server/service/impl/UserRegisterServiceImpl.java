package cn.fudges.server.service.impl;

import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.entity.UserBase;
import cn.fudges.server.entity.UserPassword;
import cn.fudges.server.request.UserPasswordRegisterRequest;
import cn.fudges.server.service.UserRegisterService;
import cn.fudges.server.service.UserBaseService;
import cn.fudges.server.service.UserPasswordService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
        AssertUtils.isNotNull(request.getMobilePhone(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getPassword(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getLength(), ResultCodeEnum.PARAM_ERROR);

        LambdaQueryWrapper<UserBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserBase::getMobilePhone, request.getMobilePhone());
        UserBase userBase = userBaseService.getOne(wrapper);
        AssertUtils.isNull(userBase, ResultCodeEnum.PARAM_ERROR, "手机号码已经存在");

        UserBase user = BeanUtil.copyProperties(request, UserBase.class);
        user.setName("U_" + RandomUtil.randomString(8));
        user.setCreateTime(LocalDateTime.now());
        user.setModifyTime(LocalDateTime.now());
        userBaseService.save(user);

        UserPassword userPassword = BeanUtil.copyProperties(request, UserPassword.class);
        userPassword.setUserId(user.getId());
        userPassword.setSalt(RandomUtil.randomString(10));
        userPassword.setPassword(DigestUtil.md5Hex(userPassword.getPassword() + userPassword.getSalt()));
        userPasswordService.save(userPassword);
        return user;
    }
}
