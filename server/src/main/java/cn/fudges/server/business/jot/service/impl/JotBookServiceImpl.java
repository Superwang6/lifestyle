package cn.fudges.server.business.jot.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.business.jot.service.JotRecordService;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.business.jot.entity.JotBook;
import cn.fudges.server.business.jot.entity.JotClassify;
import cn.fudges.server.business.jot.mapper.JotBookMapper;
import cn.fudges.server.business.jot.request.JotBookRequest;
import cn.fudges.server.business.jot.service.JotBookService;
import cn.fudges.server.business.jot.service.JotClassifyService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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
@Slf4j
@RequiredArgsConstructor
public class JotBookServiceImpl extends ServiceImpl<JotBookMapper, JotBook> implements JotBookService {

    private final JotClassifyService jotClassifyService;

    private final JotRecordService jotRecordService;


    @Override
    public IPage<JotBook> queryPage(JotBookRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        QueryWrapper<JotBook> wrapper = new QueryWrapper<>();
        Map<String, Object> map = BeanUtil.beanToMap(request, true, true);
        wrapper.allEq(map).orderByAsc("order_num", "id");
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

        JotBook jotBook = BeanUtil.copyProperties(request, JotBook.class);
        jotBook.setOrderNum(1000);
        return save(jotBook);
    }

    @Override
    public Boolean modifyJotBook(JotBookRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getName(), ResultCodeEnum.PARAM_ERROR);

        JotBook dbBook = getById(request.getId());
        AssertUtils.isNotNull(dbBook, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isTrue(dbBook.getUserId().equals(request.getUserId()), ResultCodeEnum.PERMISSION_DENIED);
        JotBook jotBook = BeanUtil.copyProperties(request, JotBook.class);
        return updateById(jotBook);
    }

    @Transactional
    @Override
    public Boolean deleteJotBook(Long id, Long userId) {
        AssertUtils.isNotNull(id, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(userId, ResultCodeEnum.PARAM_ERROR);

        JotBook dbBook = getById(id);
        AssertUtils.isNotNull(dbBook, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isTrue(dbBook.getUserId().equals(userId), ResultCodeEnum.PERMISSION_DENIED);

        boolean result = removeById(id);
        if(result) {
            jotClassifyService.deleteByBookId(id);
            jotRecordService.deleteByBookId(id);
        }
        return result;
    }

    @Transactional
    @Override
    public void initJotModule(Long userId) {
        AssertUtils.isNotNull(userId, ResultCodeEnum.PARAM_ERROR);

        LambdaQueryWrapper<JotBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(JotBook::getUserId, userId);
        List<JotBook> list = list(wrapper);
        if(CollUtil.isEmpty(list)) {
            log.info("first init jot book: {}", userId);
            JotBook book = new JotBook();
            book.setName("备忘本");
            book.setUserId(userId);
            book.setOrderNum(1000);
            book.setDescription("默认备忘本");
            save(book);

            JotClassify classify = new JotClassify();
            classify.setName("默认分类");
            classify.setBookId(book.getId());
            classify.setUserId(userId);
            jotClassifyService.save(classify);
        }
    }
}
