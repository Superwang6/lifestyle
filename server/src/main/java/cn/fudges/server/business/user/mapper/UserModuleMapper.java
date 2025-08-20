package cn.fudges.server.business.user.mapper;

import cn.fudges.server.business.user.entity.UserModule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户模块关系表 Mapper 接口
 * </p>
 *
 * @author wpy
 * @since 2025-08-12
 */
public interface UserModuleMapper extends BaseMapper<UserModule> {

    List<UserModule> queryUserModuleList(Long userId);
}
