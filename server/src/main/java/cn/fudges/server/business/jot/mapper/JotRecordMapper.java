package cn.fudges.server.business.jot.mapper;

import cn.fudges.server.business.jot.entity.JotRecord;
import cn.fudges.server.business.jot.request.JotRecordRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 备忘记录 Mapper 接口
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
public interface JotRecordMapper extends BaseMapper<JotRecord> {

    IPage<JotRecord> queryPageList(IPage<JotRecord> page, @Param("request") JotRecordRequest request);
}
