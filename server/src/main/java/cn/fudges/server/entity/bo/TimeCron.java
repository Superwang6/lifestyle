package cn.fudges.server.entity.bo;

import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.utils.AssertUtils;
import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

/**
 * @author 王平远
 * @since 2025/7/2
 */
@Data
public class TimeCron {

    /**
     * 秒
     */
    private String second = "0";

    /**
     * 步进秒
     */
    private Integer stepSecond;

    /**
     * 分钟
     */
    private String minute;

    /**
     * 步进分钟
     */
    private Integer stepMinute;

    /**
     * 小时
     */
    private String hour;

    /**
     * 步进小时
     */
    private Integer stepHour;

    /**
     * 月
     */
    private String month;

    /**
     * 步进月份
     */
    private Integer stepMonth;

    /**
     * 星期，周一至周日，逗号分隔，1-7，例如：2,3,4 表示 周二、周三、周四
     */
    private String dayOfWeek;

    /**
     * 步进天-星期
     */
    private Integer stepDayOfWeek;

    /**
     * 天，逗号分隔，例如：1,20 表示 每月的 1 号和 20 号
     */
    private String dayOfMonth;

    /**
     * 步进天-月份
     */
    private Integer stepDayOfMonth;

    /**
     * 触发次数
     */
    private Integer triggerTimes;

    public String toCron() {
        AssertUtils.isTrue(!(ObjectUtil.isNotNull(dayOfMonth) && ObjectUtil.isNotNull(dayOfWeek)), ResultCodeEnum.PARAM_ERROR);

        StringBuilder sb = new StringBuilder();
        sb.append(ObjectUtil.defaultIfNull(second, "*"));
        if (stepSecond != null) {
            sb.append("/").append(stepSecond);
        }
        sb.append(" ");
        sb.append(ObjectUtil.defaultIfNull(minute, "*"));
        if (stepMinute != null) {
            sb.append("/").append(stepMinute);
        }
        sb.append(" ");
        sb.append(ObjectUtil.defaultIfNull(hour, "*"));
        if (stepHour != null) {
            sb.append("/").append(stepHour);
        }
        sb.append(" ");
        if (dayOfWeek == null) {
            sb.append(ObjectUtil.defaultIfNull(dayOfMonth, "*"));
            if (stepDayOfMonth != null) {
                sb.append("/").append(stepDayOfMonth);
            }
        } else {
            sb.append("?");
        }
        sb.append(" ");
        sb.append(ObjectUtil.defaultIfNull(month, "*"));
        if (stepMonth != null) {
            sb.append("/").append(stepMonth);
        }
        sb.append(" ");
        if(dayOfMonth == null) {
            sb.append(ObjectUtil.defaultIfNull(dayOfWeek, "*"));
            if (stepDayOfWeek != null) {
                sb.append("/").append(stepDayOfWeek);
            }
        } else {
            sb.append("?");
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        TimeCron timeCron = new TimeCron();
        timeCron.setSecond("10");
        timeCron.setMinute("2");
        timeCron.setStepSecond(2);
        timeCron.setDayOfMonth("1,2");
        timeCron.setDayOfWeek("1");
        System.out.println(timeCron.toCron());
    }
}
