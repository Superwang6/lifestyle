package cn.fudges.server.service.impl;

import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.entity.PushRecord;
import cn.fudges.server.entity.UserBase;
import cn.fudges.server.mapper.PushRecordMapper;
import cn.fudges.server.request.PushRecordRequest;
import cn.fudges.server.service.PushRecordService;
import cn.fudges.server.service.UserBaseService;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 推送记录 服务实现类
 * </p>
 *
 * @author wpy
 * @since 2025-06-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PushRecordServiceImpl extends ServiceImpl<PushRecordMapper, PushRecord> implements PushRecordService {

    public static final String PUSH_URL = "https://fc-mp-821da366-0775-451d-a73a-1118c8627b10.next.bspapp.com/sendUniPush";

    private final UserBaseService userBaseService;

    @Override
    public boolean sendPushRecord(PushRecordRequest pushRecordRequest) {
        AssertUtils.isNotNull(pushRecordRequest, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotBlank(pushRecordRequest.getContent(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(pushRecordRequest.getTargetUserId(), ResultCodeEnum.PARAM_ERROR);

        PushRecord pushRecord = new PushRecord();
        pushRecord.setUrl(PUSH_URL);
        pushRecord.setTargetUserId(pushRecordRequest.getTargetUserId());
        UserBase user = userBaseService.getById(pushRecordRequest.getTargetUserId());
        pushRecord.setTargetId(user.getUniPushCid());
        pushRecord.setTargetType(0);
        // 0 暂认为是系统推送
        pushRecord.setSourceUserId(0L);

        Dict param = Dict.create()
                .set("cid", pushRecord.getTargetId())
                .set("content", pushRecordRequest.getContent())
                .set("payload", pushRecordRequest.getPayload());
        if(StrUtil.isNotBlank(pushRecordRequest.getTitle())) {
            param.set("title", pushRecordRequest.getTitle());
        }
        pushRecord.setRequest(JSONUtil.toJsonStr(param));

        return sendPushRecord(pushRecord);
    }

    private boolean sendPushRecord(PushRecord pushRecord) {
        log.info("PushRecordService.sendPushRecord: {}", pushRecord);
        AssertUtils.isNotNull(pushRecord, ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotBlank(pushRecord.getRequest(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotBlank(pushRecord.getUrl(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(pushRecord.getSourceUserId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(pushRecord.getTargetId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(pushRecord.getTargetUserId(), ResultCodeEnum.PARAM_ERROR);
        AssertUtils.isNotNull(pushRecord.getTargetType(), ResultCodeEnum.PARAM_ERROR);

        // 发送推送
        String result = HttpUtil.post(pushRecord.getUrl(), pushRecord.getRequest());

        pushRecord.setResponse(result);
        JSONObject resultObj = JSONUtil.parseObj(result);
        Integer errCode = resultObj.getInt("errCode");
        pushRecord.setResultCode(errCode);
        pushRecord.setCreateTime(LocalDateTime.now());
        save(pushRecord);
        return true;
    }
}
