package cn.fudges.server.enums;

import lombok.Getter;

/**
 * @author 王平远
 * @since 2025/7/3
 */
@Getter
public enum ScheduleTaskBusinessType {

    JOT_REMIND(0, "备忘录");

    private final Integer code;

    private final String desc;

    ScheduleTaskBusinessType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
