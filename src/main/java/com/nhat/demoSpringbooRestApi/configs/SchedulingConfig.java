package com.nhat.demoSpringbooRestApi.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler());
        taskRegistrar.addCronTask(new CronTask(this::sendDailyNotification, "0 0 12 * * ?"));
        // Thêm các lịch khác nếu cần
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5); // Số lượng luồng trong pool
        taskScheduler.setThreadNamePrefix("my-scheduled-task-pool-");
        taskScheduler.initialize();
        return taskScheduler;
    }

    public void sendDailyNotification() {
        // Gửi thông báo hàng ngày
        System.out.println("Sending daily notification in thread: " + Thread.currentThread().getName());
        // Thêm logic gửi thông báo ở đây
    }
}