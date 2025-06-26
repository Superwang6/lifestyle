package cn.fudges.server.enums;

/**
 * @author 王平远
 * @since 2025/6/26
 */

public enum PushRecordTypeEnum {
    JOT_REMIND(1, "备忘录提醒");


    private Integer type;

    private String title;

    PushRecordTypeEnum(Integer type, String title) {
        this.type = type;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Integer getType() {
        return type;
    }
}
