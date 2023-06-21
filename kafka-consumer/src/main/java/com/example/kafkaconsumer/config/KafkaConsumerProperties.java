package com.example.kafkaconsumer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.kafka.consumer")
@Getter
@Setter
class KafkaConsumerProperties {
    private String bootstrapServers;
    private String topic;
    private String groupId;
    private String clientId;
    private boolean autoCommit;
}

