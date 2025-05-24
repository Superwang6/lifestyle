package cn.fudges.server.common.exception;

import cn.fudges.server.common.result.IResultCodeEnum;

/**
 * @author 王平远
 * @since 2024/8/20
 */

public class BusinessException extends RuntimeException {

    private String code;

    private String message;

    public BusinessException(String code, String message) {
        super(code + ":" + message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(IResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getCode() + ":" + resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    public BusinessException(IResultCodeEnum resultCodeEnum, String message) {
        super(resultCodeEnum.getCode() + ":" + message);
        this.code = resultCodeEnum.getCode();
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
