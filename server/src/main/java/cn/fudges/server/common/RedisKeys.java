package cn.fudges.server.common;

/**
 * @author 王平远
 * @since 2025/7/24
 */

public class RedisKeys {
    // =====================================缓存====================================================

    public static final String FILE_REFRESH_ID_KEY = "file:refresh:id:";

    public static final String FILE_REFRESH_SIGN_KEY = "file:refresh:sign:";

    public static final String SMS_SEND_KEY = "sms_send:";


    // =====================================队列====================================================

    public static final String DEAD_LETTER_QUEUE = "dead_letter_queue:";

    public static final String USER_BASE_CREATE_QUEUE = "user_base:create_queue";

    public static final String SCHEDULE_TASK_CHANGE_QUEUE = "schedule_task:change_queue";

    public static final String JOT_REMIND_SEND_MESSAGE_QUEUE = "jot_remind:send_message_queue";

    public static final String USER_MODULE_UPDATE_MODULE_QUEUE = "user_module:update_module_queue";
}
