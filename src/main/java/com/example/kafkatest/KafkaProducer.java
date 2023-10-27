package com.example.kafkatest;

import com.example.kafkatest.dto.DocumentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, DocumentDto> kafkaTemplate;

    @Value("${kafka.topic-names.documents-in}")
    private String topic;


    public ProducerRecord<String, DocumentDto> send(DocumentDto documentDto) {
        ListenableFuture<SendResult<String, DocumentDto>> future = kafkaTemplate.send(topic, documentDto);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, DocumentDto> result) {
                log.info("Message sent to topic {}", result.getRecordMetadata().topic());
            }

            @Override
            public void onFailure(Throwable ex) {
                throw new RuntimeException("Failed to send message", ex);
            }
        });

        try {
            SendResult<String, DocumentDto> result = future.get();
            return result.getProducerRecord();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }
}
