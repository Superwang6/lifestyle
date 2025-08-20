package cn.fudges.server.business.sms.enums;

/**
 * @author 王平远
 * @since 2025/8/6
 */
public enum SmsBusinessEnum {

    /**
     * 短信登录验证码
     */
    loginCode,
    /**
     * 忘记密码验证码
     */
    forgetPasswordCode,
    /**
     * 修改手机号验证码
     */
    modifyMobilePhoneCode;

    public static boolean check(String value) {
        try {
            SmsBusinessEnum.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
