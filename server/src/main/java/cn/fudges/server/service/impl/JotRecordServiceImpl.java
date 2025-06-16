package cn.fudges.server.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.entity.JotRecord;
import cn.fudges.server.mapper.JotRecordMapper;
import cn.fudges.server.request.JotRecordRequest;
import cn.fudges.server.service.JotRecordService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
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
@RequiredArgsConstructor
public class JotRecordServiceImpl extends ServiceImpl<JotRecordMapper, JotRecord> implements JotRecordService {

    private final JotRecordMapper jotRecordMapper;

    @Override
    public IPage<JotRecord> queryPage(JotRecordRequest request) {
        request.setUserId(StpUtil.getLoginIdAsLong());
        if(request.getTimeType() != null) {
            switch (request.getTimeType()) {
                case 0:
                    request.setStartTime(DateUtil.beginOfDay(new Date()).toLocalDateTime());
                    request.setEndTime(DateUtil.endOfDay(new Date()).toLocalDateTime());
                    break;
                case 1:
                    request.setStartTime(DateUtil.beginOfDay(new Date()).toLocalDateTime());
                    request.setEndTime(DateUtil.endOfDay(DateUtil.offsetDay(new Date(), 3)).toLocalDateTime());
                    break;
                case 2:
                    request.setStartTime(DateUtil.beginOfDay(new Date()).toLocalDateTime());
                    request.setEndTime(DateUtil.endOfDay(DateUtil.offsetDay(new Date(), 7)).toLocalDateTime());
                    break;
                case 3:
                    request.setStartTime(DateUtil.endOfDay(new Date()).toLocalDateTime());
                    break;
                case 4:
                    request.setEndTime(DateUtil.beginOfDay(new Date()).toLocalDateTime());
                    break;
            }
        }
        return jotRecordMapper.queryPageList(request.getPage(), request);
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

    @Override
    public Boolean delay(JotRecordRequest request) {
        AssertUtils.isNotNull(request, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(request.getDelayDays(), ResultCodeEnum.PARAM_ERROR);

        JotRecord record = getById(request.getId());
        AssertUtils.isNotNull(record, ResultCodeEnum.BUSINESS_EXCEPTION);
        AssertUtils.isTrue(record.getUserId().equals(StpUtil.getLoginIdAsLong()), ResultCodeEnum.PERMISSION_DENIED);

        LocalDateTime newRemindTime = record.getRemindTime().plusDays(request.getDelayDays());
        JotRecord modifyRecord = new JotRecord();
        modifyRecord.setId(request.getId());
        modifyRecord.setRemindTime(newRemindTime);
        modifyRecord.setModifyTime(LocalDateTime.now());
        return updateById(modifyRecord);
    }
}
