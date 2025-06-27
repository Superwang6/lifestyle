package cn.fudges.server.filter;

import cn.hutool.core.util.IdUtil;
import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author 王平远
 * @since 2025/6/27
 */
@Component
public class LogTraceFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            String traceId = IdUtil.fastSimpleUUID();
            MDC.put("traceId", traceId);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
        }
    }
}
