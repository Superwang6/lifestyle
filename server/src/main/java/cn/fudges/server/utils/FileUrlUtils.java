package cn.fudges.server.utils;

import cn.fudges.server.config.properties.ProjectConfigProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author 王平远
 * @since 2025/7/25
 */
@Component
@RequiredArgsConstructor
public class FileUrlUtils {

    private static String domain;

    private static String prePath;

    private final ProjectConfigProperties projectConfigProperties;

    @PostConstruct
    public void init() {
        FileUrlUtils.domain = projectConfigProperties.getDomain();
        FileUrlUtils.prePath = projectConfigProperties.getFile().getPrePath();
    }

    /**
     * 生成文件的完整路径，并设置到实体类中
     * @param entity 操作的实体类对象
     * @param getter 获取字段
     * @param setter 设置字段
     * @param <T>
     */
    public static <T> void generateFullUrl(T entity, Function<T, String> getter, BiConsumer<T, String> setter) {
        String apply = getter.apply(entity);
        String url = domain + prePath + apply;
        setter.accept(entity, url);
    }
}
