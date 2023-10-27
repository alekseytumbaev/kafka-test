package com.example.kafkatest;

import com.example.kafkatest.dto.ProcessingResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {
    @KafkaListener(topics = "${kafka.topic-names.documents-out}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void receive(@Payload ProcessingResultDto resultDto) {
        log.info("RECEIVED: {}", resultDto);
    }
}
