package cn.fudges.server.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.entity.JotBook;
import cn.fudges.server.mapper.JotBookMapper;
import cn.fudges.server.request.JotBookRequest;
import cn.fudges.server.service.JotBookService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 备忘本 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
@Service
public class JotBookServiceImpl extends ServiceImpl<JotBookMapper, JotBook> implements JotBookService {

    @Override
    public IPage<JotBook> queryPage(JotBookRequest request) {
        QueryWrapper<JotBook> wrapper = new QueryWrapper<>();
        Map<String, Object> map = BeanUtil.beanToMap(request, true, true);
        wrapper.allEq(map);
        return page(request.getPage(), wrapper);
    }

    @Override
    public Boolean addJotBook(JotBookRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getName(), ResultCodeEnum.PARAM_ERROR);

        long userId = StpUtil.getLoginIdAsLong();
        JotBook jotBook = BeanUtil.copyProperties(request, JotBook.class);
        jotBook.setUserId(userId);
        return save(jotBook);
    }
}
