package cn.fudges.server.enums;

import lombok.Getter;

/**
 * @author 王平远
 * @since 2025/7/2
 */

@Getter
public enum JotRemindTypeEnum {
    ONCE(0, "单次提醒"),
    CYCLE(1, "周期提醒");

    private final int code;
    private final String desc;

    JotRemindTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static JotRemindTypeEnum getByCode(int code) {
        for (JotRemindTypeEnum type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid remind type code: " + code);
    }

}
