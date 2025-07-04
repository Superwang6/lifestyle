package cn.fudges.server.config;

import cn.fudges.server.utils.MdcTaskDecorator;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @author 王平远
 * @since 2025/6/27
 */
@Configuration
@EnableScheduling
public class ScheduleConfigure implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("schedule-");
        scheduler.initialize();
        scheduler.setTaskDecorator(new MdcTaskDecorator());
        registrar.setTaskScheduler(scheduler);
    }
}
