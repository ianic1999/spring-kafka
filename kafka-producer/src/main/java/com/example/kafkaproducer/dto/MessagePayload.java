package com.example.kafkaproducer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessagePayload {
    private String message;
    private final LocalDateTime sentAt = LocalDateTime.now();
}
