package cn.fudges.server.service.inner;

import cn.fudges.server.entity.JotRecord;
import cn.fudges.server.request.JotRecordRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 备忘记录 服务类
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
public interface JotRecordService extends IService<JotRecord> {

    IPage<JotRecord> queryPage(JotRecordRequest request);

    Boolean addJotRecord(JotRecordRequest request);

    Boolean modifyJotRecord(JotRecordRequest request);

    Boolean delete(String id);

    Boolean delay(JotRecordRequest request);

    void jotRemindJob();

}
