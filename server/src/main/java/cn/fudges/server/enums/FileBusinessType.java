package cn.fudges.server.enums;

/**
 * @author 王平远
 * @since 2025/7/25
 */

public enum FileBusinessType {

    /**
     * 用户头像
     */
    userAvatar,
    /**
     * 用户私有文件
     */
    userPrivate
    ;

    public static boolean check(String value) {
        try {
            FileBusinessType.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
