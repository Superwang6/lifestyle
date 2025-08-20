package cn.fudges.server.initialize.queue;

import cn.fudges.server.common.RedisKeys;
import cn.fudges.server.common.entity.DeadLetter;
import cn.fudges.server.utils.MdcTaskDecorator;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author 王平远
 * @since 2025/8/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MultiQueueConsumerInitialize implements SmartInitializingSingleton {

    private ThreadPoolTaskExecutor executor;

    private volatile boolean running = true;

    private final RedissonClient redissonClient;

    private final ApplicationContext applicationContext;

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, QueueConsumer> queueConsumerMap = applicationContext.getBeansOfType(QueueConsumer.class);

        if(CollUtil.isNotEmpty(queueConsumerMap.values())) {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(queueConsumerMap.values().size());
            executor.setMaxPoolSize(queueConsumerMap.values().size());
            executor.setQueueCapacity(1000);
            executor.setThreadNamePrefix("queue-");
            executor.setTaskDecorator(new MdcTaskDecorator());
            executor.setWaitForTasksToCompleteOnShutdown(true);
            executor.initialize();
            this.executor = executor;

            for (QueueConsumer queueConsumer : queueConsumerMap.values()) {
                if(StrUtil.isNotBlank(queueConsumer.queueName())) {
                    registerQueue(queueConsumer.queueName(), queueConsumer);
                } else {
                    log.warn("init {} queue fail, queueName is blank", queueConsumer.getClass().getName());
                }
            }
        }
    }

    public <T> void registerQueue(String queueName, QueueConsumer<T> queueConsumer) {
        RBlockingQueue<DeadLetter<T>> dlq = redissonClient.getBlockingQueue(RedisKeys.DEAD_LETTER_QUEUE + queueName);

        RBlockingQueue<T> blockingQueue = redissonClient.getBlockingQueue(queueName);
        executor.submit(() -> {
            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    T take = blockingQueue.take();
                    if(running && !Thread.currentThread().isInterrupted()) {
                        log.info("[{}] consume message: {}", queueName, take);
                        try {
                            // 消费
                            queueConsumer.consume(take);
                        } catch (Exception e) {
                            log.error(StrUtil.format("{} consume fail", queueName), e);
                            // 转移到死信队列
                            DeadLetter<T> deadLetter = new DeadLetter<>();
                            deadLetter.setOriginQueue(queueName);
                            deadLetter.setMessage(take);
                            deadLetter.setCreateTime(LocalDateTime.now());
                            deadLetter.setRetryCount(0);
                            dlq.offer(deadLetter);
                        }
                    } else {
                        // 关机，重新放回队列中
                        log.info("[{}] is not running, offer back message: {}", queueName, take);
                        blockingQueue.offer(take);
                    }
                } catch (InterruptedException e) {
                    log.error(StrUtil.format("[{}] take fail", queueName), e);
                    break;
                }
            }
        });
    }

    @PreDestroy
    public void destroy() {
        running = false;
    }


}
