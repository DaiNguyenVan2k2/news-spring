package com.ptit.news.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class AxonConfig {

    @Autowired
    public void configureEventProcessing(EventProcessingConfigurer configurer) {
        configurer.usingSubscribingEventProcessors();
    }

    @Bean
    public EventStore eventStore() {
        return EmbeddedEventStore.builder()
                .storageEngine(new InMemoryEventStorageEngine())
                .build();
    }
}