package com.example.kafkaproducer.web;

import com.example.kafkaproducer.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final KafkaProducer kafkaProducer;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessageAsString(@RequestBody String message) {
        kafkaProducer.sendAsString(message);
        return ResponseEntity.noContent().build();
    }
}
