package cn.fudges.server.handler;

import cn.dev33.satoken.exception.SaTokenException;
import cn.fudges.server.common.exception.BusinessException;
import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.common.result.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

/**
 * @author 王平远
 * @since 2025/5/24
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    public ResultResponse<Void> handleBizException(BusinessException e) {
        return ResultResponse.fail(e.getCode(), e.getMessage(), null);
    }

    // 处理sa-token异常
    @ExceptionHandler(SaTokenException.class)
    public ResultResponse<Void> handleBizException(SaTokenException e) {
        ResultCodeEnum resultEnum = ResultCodeEnum.BUSINESS_EXCEPTION;
        List<Integer> noLoginCodes = List.of(11011, 11012, 11013);
        if(noLoginCodes.contains(e.getCode())) {
            resultEnum = ResultCodeEnum.NO_LOGIN;
        }
        return ResultResponse.fail(resultEnum.getCode(), resultEnum.getMessage(), null);
    }

    // 处理资源异常
    @ExceptionHandler(NoResourceFoundException.class)
    public ResultResponse<Void> handleNoResourceFoundException() {
        return ResultResponse.fail(ResultCodeEnum.NOT_FOUND.getCode(), ResultCodeEnum.NOT_FOUND.getMessage(), null);
    }

    // 捕获所有其他异常
    @ExceptionHandler(Exception.class)
    public ResultResponse<Void> handleException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return ResultResponse.fail(ResultCodeEnum.SYSTEM_ERROR.getCode(), ResultCodeEnum.SYSTEM_ERROR.getMessage(), null);
    }
}
