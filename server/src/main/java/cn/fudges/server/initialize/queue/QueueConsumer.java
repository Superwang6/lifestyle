package cn.fudges.server.initialize.queue;

/**
 * @author 王平远
 * @since 2025/8/5
 */
public interface QueueConsumer<T> {
    String queueName();

    void consume(T message);
}
