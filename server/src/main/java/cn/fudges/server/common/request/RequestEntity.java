package cn.fudges.server.common.request;

import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 王平远
 * @since 2025/5/23
 */
public class RequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pageNum;

    private Integer pageSize;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String search;

    public <T> IPage<T> getPage() {
        AssertUtils.isTrue(ObjectUtil.isNotNull(this.pageNum) && ObjectUtil.isNotNull(this.pageSize), ResultCodeEnum.PARAM_ERROR, "分页参数不能为空");
        return new Page<T>(this.pageNum, this.pageSize);
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
