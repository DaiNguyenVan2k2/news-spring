package com.ptit.news.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.ptit.news.repository")
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfig {

    // Các bean cấu hình database sẽ được thêm ở đây nếu cần
}