package cn.fudges.server.common.result;

/**
 * @author 王平远
 * @since 2024/8/20
 */

public enum ResultCodeEnum implements IResultCodeEnum {

    SUCCESS("0000", "请求成功"),
    BUSINESS_EXCEPTION("1000", "业务异常"),
    NO_LOGIN("1001", "未登录，请重新登录！"),
    PARAM_ERROR("1002", "参数错误"),
    PERMISSION_DENIED("1003", "权限不足"),
    ACCOUNT_PASSWORD_ERROR("1004", "账号或密码错误"),
    NOT_FOUND("1005", "资源不存在"),
    SMS_CODE_ERROR("1006", "验证码错误"),
    SYSTEM_ERROR("9999", "服务繁忙，请稍后再试！"),
    ;

    private final String code;

    private final String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
