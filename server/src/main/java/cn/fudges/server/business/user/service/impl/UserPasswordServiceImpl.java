package cn.fudges.server.business.user.service.impl;

import cn.fudges.server.common.RedisKeys;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.business.user.entity.UserBase;
import cn.fudges.server.business.user.entity.UserPassword;
import cn.fudges.server.business.sms.enums.SmsBusinessEnum;
import cn.fudges.server.business.user.mapper.UserPasswordMapper;
import cn.fudges.server.business.user.request.UserPasswordRequest;
import cn.fudges.server.business.user.service.UserBaseService;
import cn.fudges.server.business.user.service.UserPasswordService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
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

    private final RedissonClient redissonClient;

    private final UserBaseService userBaseService;

    @Override
    public Boolean modifyUserPassword(UserPasswordRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getUserId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getPassword(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getLength(), ResultCodeEnum.PARAM_ERROR);

        LambdaQueryWrapper<UserPassword> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPassword::getUserId, request.getUserId());
        UserPassword password = getOne(wrapper);

        UserPassword newPassword = new UserPassword();
        newPassword.setUserId(request.getUserId());
        newPassword.setSalt(RandomUtil.randomString(10));
        newPassword.setLength(request.getLength());
        newPassword.setPassword(DigestUtil.md5Hex(request.getPassword() + newPassword.getSalt()));
        if(ObjectUtil.isNotNull(password)) {
            AssertUtils.isNotNull(request.getOldPassword(), ResultCodeEnum.PARAM_ERROR, "请输入原密码");
            AssertUtils.isTrue(request.getOldPassword().equals(password.getPassword()), ResultCodeEnum.PARAM_ERROR, "原密码不正确");
            newPassword.setId(password.getId());
            return updateById(newPassword);
        }
        return save(newPassword);
    }

    @Override
    public Boolean forgetPassword(UserPasswordRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotBlank(request.getMobilePhone(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isTrue(PhoneUtil.isMobile(request.getMobilePhone()), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotBlank(request.getCode(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotBlank(request.getPassword(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getLength(), ResultCodeEnum.PARAM_ERROR);

        LambdaQueryWrapper<UserBase> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(UserBase::getMobilePhone, request.getMobilePhone());
        UserBase userBase = userBaseService.getOne(userWrapper);
        AssertUtils.isNotNull(userBase, ResultCodeEnum.PARAM_ERROR, "手机号不存在");

        LambdaQueryWrapper<UserPassword> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPassword::getUserId, userBase.getId());
        UserPassword password = getOne(wrapper);
        AssertUtils.isNotNull(password, ResultCodeEnum.BUSINESS_EXCEPTION, "未设置密码");

        RMapCache<String, String> mapCache = redissonClient.getMapCache(RedisKeys.SMS_SEND_KEY + SmsBusinessEnum.forgetPasswordCode.name());
        String code = mapCache.get(request.getMobilePhone());
        AssertUtils.isTrue(request.getCode().equals(code), ResultCodeEnum.SMS_CODE_ERROR);
        mapCache.remove(request.getMobilePhone());

        UserPassword newPassword = new UserPassword();
        newPassword.setId(password.getId());
        newPassword.setUserId(password.getUserId());
        newPassword.setSalt(RandomUtil.randomString(10));
        newPassword.setLength(request.getLength());
        newPassword.setPassword(DigestUtil.md5Hex(request.getPassword() + newPassword.getSalt()));
        return updateById(newPassword);
    }
}
