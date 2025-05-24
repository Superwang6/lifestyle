package cn.fudges.server.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.entity.JotRecord;
import cn.fudges.server.mapper.JotRecordMapper;
import cn.fudges.server.request.JotRecordRequest;
import cn.fudges.server.service.JotRecordService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 备忘记录 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
@Service
public class JotRecordServiceImpl extends ServiceImpl<JotRecordMapper, JotRecord> implements JotRecordService {

    @Override
    public void checkJotClassifyHasRecord(Long classifyId) {
        LambdaQueryWrapper<JotRecord> recordQuery = new LambdaQueryWrapper<>();
        recordQuery.eq(JotRecord::getClassifyId, classifyId);
        JotRecord record = getOne(recordQuery);
        AssertUtils.isNull(record, ResultCodeEnum.BUSINESS_EXCEPTION, "存在备忘记录");
    }

    @Override
    public IPage<JotRecord> queryPage(JotRecordRequest request) {
        Map<String, Object> map = BeanUtil.beanToMap(request, true,true);

        QueryWrapper<JotRecord> recordQuery = new QueryWrapper<>();
        recordQuery.allEq(map);
        return page(request.getPage(), recordQuery);
    }

    @Override
    public Boolean addJotRecord(JotRecordRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getTitle(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getBookId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getStatus(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getRemindTime(), ResultCodeEnum.PARAM_ERROR);
        JotRecord jotRecord = BeanUtil.copyProperties(request, JotRecord.class);
        jotRecord.setUserId(StpUtil.getLoginIdAsLong());
        jotRecord.setCreateTime(LocalDateTime.now());
        jotRecord.setModifyTime(LocalDateTime.now());
        return save(jotRecord);
    }

    @Override
    public Boolean modifyJotRecord(JotRecordRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getId(), ResultCodeEnum.PARAM_ERROR);

        JotRecord record = getById(request.getId());
        AssertUtils.isNotNull(record, ResultCodeEnum.BUSINESS_EXCEPTION);
        AssertUtils.isTrue(record.getUserId().equals(StpUtil.getLoginIdAsLong()), ResultCodeEnum.PERMISSION_DENIED);

        JotRecord jotRecord = BeanUtil.copyProperties(request, JotRecord.class);
        jotRecord.setUserId(null);
        jotRecord.setModifyTime(LocalDateTime.now());
        return updateById(jotRecord);
    }

    @Override
    public Boolean delete(String id) {
        AssertUtils.isNotNull(id, ResultCodeEnum.PARAM_ERROR);

        JotRecord record = getById(id);
        AssertUtils.isNotNull(record, ResultCodeEnum.BUSINESS_EXCEPTION);
        AssertUtils.isTrue(record.getUserId().equals(StpUtil.getLoginIdAsLong()), ResultCodeEnum.PERMISSION_DENIED);
        return removeById(id);
    }
}
