package cn.fudges.server.config.properties;

import lombok.Data;

import java.util.List;

/**
 * @author 王平远
 * @since 2025/7/25
 */
@Data
public class FileConfigProperties {

    private String uploadDir;

    private String prePath;

    private List<String> authExclude;
}
