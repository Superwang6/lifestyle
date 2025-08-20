package cn.fudges.server.business.jot.service.impl;

import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.business.jot.entity.JotClassify;
import cn.fudges.server.business.jot.mapper.JotClassifyMapper;
import cn.fudges.server.business.jot.request.JotClassifyRequest;
import cn.fudges.server.business.jot.service.JotClassifyService;
import cn.fudges.server.business.jot.service.JotRecordService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 备忘录分类 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
@Service
@RequiredArgsConstructor
public class JotClassifyServiceImpl extends ServiceImpl<JotClassifyMapper, JotClassify> implements JotClassifyService {

    private final JotRecordService jotRecordService;

    @Override
    public IPage<JotClassify> queryPage(JotClassifyRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getUserId(), ResultCodeEnum.PARAM_ERROR);

        QueryWrapper<JotClassify> wrapper = new QueryWrapper<>();
        Map<String, Object> map = BeanUtil.beanToMap(request, true, true);
        wrapper.allEq(map);
        return page(request.getPage(), wrapper);
    }

    @Override
    public Boolean add(JotClassifyRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getBookId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getUserId(), ResultCodeEnum.PARAM_ERROR);

        LambdaQueryWrapper<JotClassify> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(JotClassify::getBookId, request.getBookId()).eq(JotClassify::getUserId, request.getUserId());
        long count = count(wrapper);
        AssertUtils.isTrue(count <= 10 , ResultCodeEnum.BUSINESS_EXCEPTION, "一个备忘录最多10个分类");
        return save(BeanUtil.copyProperties(request, JotClassify.class));
    }

    @Override
    public Boolean modify(JotClassifyRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getId(), ResultCodeEnum.PARAM_ERROR);

        return updateById(BeanUtil.copyProperties(request, JotClassify.class));
    }

    @Override
    public Boolean delete(Long id, Long userId) {
        AssertUtils.isNotNull(id, ResultCodeEnum.PARAM_ERROR);

        JotClassify jotClassify = getById(id);
        AssertUtils.isNotNull(jotClassify, ResultCodeEnum.PARAM_ERROR);

        AssertUtils.isTrue(jotClassify.getUserId().equals(userId), ResultCodeEnum.PERMISSION_DENIED);

        return removeById(id);
    }

    @Override
    public void deleteByBookId(Long bookId) {
        AssertUtils.isNotNull(bookId, ResultCodeEnum.PARAM_ERROR);

        LambdaUpdateWrapper<JotClassify> query = new LambdaUpdateWrapper<>();
        query.eq(JotClassify::getBookId, bookId);
        remove(query);
    }
}
