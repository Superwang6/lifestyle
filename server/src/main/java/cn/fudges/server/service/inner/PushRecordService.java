package cn.fudges.server.service.inner;

import cn.fudges.server.entity.PushRecord;
import cn.fudges.server.request.PushRecordRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 推送记录 服务类
 * </p>
 *
 * @author wpy
 * @since 2025-06-26
 */
public interface PushRecordService extends IService<PushRecord> {

    boolean sendPushRecord(PushRecordRequest pushRecordRequest);
}
