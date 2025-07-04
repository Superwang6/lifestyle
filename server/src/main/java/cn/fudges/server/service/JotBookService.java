package cn.fudges.server.service;

import cn.fudges.server.entity.JotBook;
import cn.fudges.server.request.JotBookRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 备忘本 服务类
 * </p>
 *
 * @author wpy
 * @since 2025-05-23
 */
public interface JotBookService extends IService<JotBook> {

    IPage<JotBook> queryPage(JotBookRequest request);

    Boolean addJotBook(JotBookRequest request);

    Boolean modifyJotBook(JotBookRequest request);
}
