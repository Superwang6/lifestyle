package cn.fudges.server.utils;

import cn.hutool.core.util.IdUtil;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * @author 王平远
 * @since 2025/6/30
 */
public class MdcTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            if (contextMap != null) {
                MDC.setContextMap(contextMap);
            } else {
                MDC.put("traceId", IdUtil.fastSimpleUUID());
            }
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
