package com.example.kafkaproducer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> stringKafkaTemplate;
    @Value("${kafka.topic.name}")
    private String stringTopic;

    public void sendAsString(String payload) {
        log.info("Sending message as STRING with payload: {}", payload);
        stringKafkaTemplate.send(stringTopic, payload);
    }
}
