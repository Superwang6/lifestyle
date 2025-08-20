package cn.fudges.server.business.common.entity.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 王平远
 * @since 2025/7/28
 */
@Data
public class Configuration implements Serializable {

    private String domain;

    private String filePrefix;

    private Boolean mobileSupport;
}
