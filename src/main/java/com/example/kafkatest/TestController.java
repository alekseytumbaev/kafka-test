package com.example.kafkatest;

import com.example.kafkatest.dto.DocumentDto;
import com.example.kafkatest.dto.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final KafkaProducer kafkaProducer;

    @GetMapping
    public void send() {
        kafkaProducer.send(DocumentDto.builder()
                .id(1L)
                .type("type1")
                .organization("org1")
                .description("descr1")
                .patient("patient1")
                .date(new Date())
                .status(Status.of("IN_PROCESS", "В процессе"))
                .build()
        );
    }
}
