package com.example.kafkaconsumer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaStringConsumer {

    @KafkaListener(topics = "${spring.kafka.consumer.topic}")
    public void listen(@Payload String message, Acknowledgment acknowledgment) {
        log.info("Received message: {}", message);
        acknowledgment.acknowledge();
    }
}
