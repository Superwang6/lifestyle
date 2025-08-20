package cn.fudges.server.business.user.service.impl;

import cn.fudges.server.business.user.entity.UserModule;
import cn.fudges.server.business.user.service.UserModuleService;
import cn.fudges.server.common.RedisKeys;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.business.user.entity.UserBase;
import cn.fudges.server.business.sms.enums.SmsBusinessEnum;
import cn.fudges.server.business.user.mapper.UserBaseMapper;
import cn.fudges.server.business.user.request.UserBaseRequest;
import cn.fudges.server.business.sms.service.SmsService;
import cn.fudges.server.business.user.service.UserBaseService;
import cn.fudges.server.utils.AssertUtils;
import cn.fudges.server.utils.queue.QueueUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    private final QueueUtils queueUtils;

    private final SmsService smsService;

    @Override
    public UserBase detail(Long id) {
        UserBase userBase = getById(id);
        if(StrUtil.isNotBlank(userBase.getMobilePhone())) {
            userBase.setMobilePhone(PhoneUtil.hideBetween(userBase.getMobilePhone()).toString());
        }
        return userBase;
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

    @Override
    public void saveUser(UserBase user) {
        user.setName("U_" + RandomUtil.randomString(8));
        user.setCreateTime(LocalDateTime.now());
        user.setModifyTime(LocalDateTime.now());
        this.save(user);

        // 创建用户 异步队列
        queueUtils.sendMessage(RedisKeys.USER_BASE_CREATE_QUEUE, user);
    }

    @Override
    public Boolean modifyAccount(UserBaseRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getAccount(), ResultCodeEnum.PARAM_ERROR);

        LambdaQueryWrapper<UserBase> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(UserBase::getAccount, request.getAccount());
        List<UserBase> list = list(checkWrapper);
        AssertUtils.isTrue(CollUtil.isEmpty(list) || (list.size() == 1 && request.getId().equals(list.get(0).getId())), ResultCodeEnum.BUSINESS_EXCEPTION, "账号已存在！");

        UserBase userBase = new UserBase();
        userBase.setId(request.getId());
        userBase.setAccount(request.getAccount());
        userBase.setModifyTime(LocalDateTime.now());
        return updateById(userBase);
    }

    @Override
    public Boolean modifyMobilePhone(UserBaseRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getMobilePhone(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getCode(), ResultCodeEnum.PARAM_ERROR);

        LambdaQueryWrapper<UserBase> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(UserBase::getMobilePhone, request.getMobilePhone());
        List<UserBase> list = list(checkWrapper);
        AssertUtils.isTrue(CollUtil.isEmpty(list) || (list.size() == 1 && request.getId().equals(list.get(0).getId())), ResultCodeEnum.BUSINESS_EXCEPTION, "该手机号已绑定其他账号！");

        smsService.checkSmsCode(request.getMobilePhone(), SmsBusinessEnum.modifyMobilePhoneCode,request.getCode());

        UserBase userBase = new UserBase();
        userBase.setId(request.getId());
        userBase.setMobilePhone(request.getMobilePhone());
        userBase.setModifyTime(LocalDateTime.now());
        return updateById(userBase);
    }

    @Override
    public String queryMobilePhone(Long id) {
        UserBase userBase = getById(id);
        return userBase.getMobilePhone();
    }


}
