package cn.fudges.server.utils;

import cn.hutool.cron.pattern.CronPattern;

/**
 * @author 王平远
 * @since 2025/7/17
 */

public class CronValidator {
    public static boolean isValid(String cron) {
        try {
            new CronPattern(cron);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
