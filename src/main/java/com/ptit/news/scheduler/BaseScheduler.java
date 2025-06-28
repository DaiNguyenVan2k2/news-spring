package com.ptit.news.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public abstract class BaseScheduler {

    /**
     * Chạy job mỗi 5 phút
     */
    @Scheduled(fixedRate = 300000) // 5 minutes
    public void runEveryFiveMinutes() {
        log.info("Running scheduled job every 5 minutes");
        executeScheduledTask();
    }

    /**
     * Chạy job mỗi giờ
     */
    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void runEveryHour() {
        log.info("Running scheduled job every hour");
        executeHourlyTask();
    }

    /**
     * Chạy job mỗi ngày lúc 00:00
     */
    @Scheduled(cron = "0 0 0 * * *") // Every day at midnight
    public void runDaily() {
        log.info("Running scheduled job daily");
        executeDailyTask();
    }

    /**
     * Override method này để implement logic cụ thể
     */
    protected abstract void executeScheduledTask();

    /**
     * Override method này để implement logic cụ thể
     */
    protected abstract void executeHourlyTask();

    /**
     * Override method này để implement logic cụ thể
     */
    protected abstract void executeDailyTask();
}