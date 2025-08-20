package cn.fudges.server.utils.queue;

import java.util.concurrent.TimeUnit;

/**
 * @author 王平远
 * @since 2025/8/6
 */

public interface QueueUtils {

    boolean sendMessage(String queueName, Object message);

    boolean sendDelayMessage(String queueName, Object message, Long delayTime, TimeUnit timeUnit);
}
