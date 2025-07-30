package cn.fudges.server.filter;

import cn.fudges.server.common.RedisKeys;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.common.result.ResultResponse;
import cn.fudges.server.config.properties.ProjectConfigProperties;
import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 王平远
 * @since 2025/6/27
 * 静态资源过滤器，用于拦截静态资源的访问请求，如果有签名则校验签名是否正确
 *
 */
@Component
@RequiredArgsConstructor
public class StaticFileFilter implements Filter {

    private final RedissonClient redissonClient;

    private final ProjectConfigProperties projectConfigProperties;

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url = request.getRequestURI();
        if (url.startsWith(projectConfigProperties.getFile().getPrePath()) && !isExclude(url)) {
            String sign = request.getParameter("sign");
            if(StrUtil.isNotBlank(sign)) {
                RBucket<Object> bucket = redissonClient.getBucket(RedisKeys.FILE_REFRESH_SIGN_KEY + sign);
                if (bucket.isExists()) {
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONUtil.toJsonStr(ResultResponse.fail(ResultCodeEnum.PERMISSION_DENIED.getCode(), ResultCodeEnum.PERMISSION_DENIED.getMessage(), null)));
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private boolean isExclude(String url) {
        for (String pattern : projectConfigProperties.getFile().getAuthExclude()) {
            if(matcher.match(pattern, url)) {
                return true;
            }
        }
        return false;
    }
}
