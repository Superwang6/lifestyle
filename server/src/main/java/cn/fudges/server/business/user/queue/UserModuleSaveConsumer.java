package cn.fudges.server.business.user.queue;

import cn.fudges.server.business.jot.service.JotBookService;
import cn.fudges.server.business.user.entity.UserModule;
import cn.fudges.server.common.RedisKeys;
import cn.fudges.server.initialize.queue.QueueConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author 王平远
 * @since 2025/8/20
 */
@Component
@RequiredArgsConstructor
public class UserModuleSaveConsumer implements QueueConsumer<UserModule> {

    private final JotBookService jotBookService;

    @Override
    public String queueName() {
        return RedisKeys.USER_MODULE_UPDATE_MODULE_QUEUE;
    }

    @Override
    public void consume(UserModule module) {
        if(module.getModuleId() == 1) {
            jotBookService.initJotModule(module.getUserId());
        }
    }
}
