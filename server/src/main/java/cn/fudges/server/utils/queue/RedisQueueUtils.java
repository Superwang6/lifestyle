package cn.fudges.server.utils.queue;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author 王平远
 * @since 2025/8/6
 */
@Component
@RequiredArgsConstructor
public class RedisQueueUtils implements QueueUtils {

    private final RedissonClient redissonClient;

    @Override
    public boolean sendMessage(String queueName, Object message) {
        return redissonClient.getBlockingQueue(queueName).offer(message);
    }

    @Override
    public boolean sendDelayMessage(String queueName, Object message, Long delayTime, TimeUnit timeUnit) {
        RBlockingQueue<Object> blockingQueue = redissonClient.getBlockingQueue(queueName);
        redissonClient.getDelayedQueue(blockingQueue).offer(message, delayTime, timeUnit);
        return true;
    }
}
