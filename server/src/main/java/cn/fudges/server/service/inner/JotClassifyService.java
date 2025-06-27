package cn.fudges.server.service.inner;

import cn.fudges.server.entity.JotClassify;
import cn.fudges.server.request.JotClassifyRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 备忘录分类 服务类
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
public interface JotClassifyService extends IService<JotClassify> {

    IPage<JotClassify> queryPage(JotClassifyRequest request);

    Boolean add(JotClassifyRequest request);

    Boolean modify(JotClassifyRequest request);

    Boolean delete(Long id);
}
