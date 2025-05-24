package cn.fudges.server.service.impl;

import cn.fudges.server.entity.UserBase;
import cn.fudges.server.mapper.UserBaseMapper;
import cn.fudges.server.service.UserBaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


}
