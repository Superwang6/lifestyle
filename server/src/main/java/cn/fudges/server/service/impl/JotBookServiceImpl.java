package cn.fudges.server.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.entity.JotBook;
import cn.fudges.server.entity.JotClassify;
import cn.fudges.server.mapper.JotBookMapper;
import cn.fudges.server.request.JotBookRequest;
import cn.fudges.server.service.JotBookService;
import cn.fudges.server.service.JotClassifyService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 备忘本 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
@Service
@RequiredArgsConstructor
public class JotBookServiceImpl extends ServiceImpl<JotBookMapper, JotBook> implements JotBookService {

    private final JotClassifyService jotClassifyService;


    @Override
    public IPage<JotBook> queryPage(JotBookRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        QueryWrapper<JotBook> wrapper = new QueryWrapper<>();
        Map<String, Object> map = BeanUtil.beanToMap(request, true, true);
        wrapper.allEq(map);
        IPage<JotBook> page = page(request.getPage(), wrapper);

        LambdaQueryWrapper<JotClassify> classifyWrapper = new LambdaQueryWrapper<>();
        classifyWrapper.eq(JotClassify::getUserId, request.getUserId()).eq(JotClassify::getIsRemove, 0);
        List<JotClassify> classifyList = jotClassifyService.list(classifyWrapper);
        Map<Long, List<JotClassify>> classifyMap = classifyList.stream().collect(Collectors.groupingBy(JotClassify::getBookId));

        page.getRecords().forEach(book -> {
            List<JotClassify> tempList = classifyMap.get(book.getId());
            book.setClassifyList(tempList);
        });
        return page;
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

    @Override
    public Boolean modifyJotBook(JotBookRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getName(), ResultCodeEnum.PARAM_ERROR);

        JotBook dbBook = getById(request.getId());
        AssertUtils.isNotNull(dbBook, ResultCodeEnum.PARAM_ERROR);
        long userId = StpUtil.getLoginIdAsLong();
        AssertUtils.isTrue(dbBook.getUserId().equals(userId), ResultCodeEnum.PERMISSION_DENIED);
        JotBook jotBook = BeanUtil.copyProperties(request, JotBook.class);
        return updateById(jotBook);
    }
}
