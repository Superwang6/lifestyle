package cn.fudges.server.business.user.queue;

import cn.fudges.server.common.RedisKeys;
import cn.fudges.server.business.user.entity.UserBase;
import cn.fudges.server.initialize.queue.QueueConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author 王平远
 * @since 2025/8/6
 *
 * 用户创建副作用队列
 */
@Component
@RequiredArgsConstructor
public class UserBaseCreateConsumer implements QueueConsumer<UserBase> {

    @Override
    public String queueName() {
        return RedisKeys.USER_BASE_CREATE_QUEUE;
    }

    @Override
    public void consume(UserBase message) {
        //TODO

    }
}
