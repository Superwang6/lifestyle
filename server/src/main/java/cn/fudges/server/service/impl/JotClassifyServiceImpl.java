package cn.fudges.server.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.entity.JotClassify;
import cn.fudges.server.mapper.JotClassifyMapper;
import cn.fudges.server.request.JotClassifyRequest;
import cn.fudges.server.service.JotClassifyService;
import cn.fudges.server.service.JotRecordService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return page(request.getPage());
    }

    @Override
    public Boolean add(JotClassifyRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getBookId(), ResultCodeEnum.PARAM_ERROR);

        request.setUserId(StpUtil.getLoginIdAsLong());
        return save(BeanUtil.copyProperties(request, JotClassify.class));
    }

    @Override
    public Boolean modify(JotClassifyRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getId(), ResultCodeEnum.PARAM_ERROR);

        return updateById(BeanUtil.copyProperties(request, JotClassify.class));
    }

    @Override
    public Boolean delete(Long id) {
        AssertUtils.isNotNull(id, ResultCodeEnum.PARAM_ERROR);

        JotClassify jotClassify = getById(id);
        AssertUtils.isNotNull(jotClassify, ResultCodeEnum.PARAM_ERROR);

        AssertUtils.isTrue(jotClassify.getUserId().equals(StpUtil.getLoginIdAsLong()), ResultCodeEnum.PERMISSION_DENIED);

        return removeById(id);
    }

}
