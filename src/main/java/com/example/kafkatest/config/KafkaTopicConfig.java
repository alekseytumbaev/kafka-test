package com.example.kafkatest.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topic-names.documents-in}")
    private String documentsIn;

    @Value("${kafka.topic-names.documents-out}")
    private String documentsOut;


    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    /**
     * Создает топик, в который записываются документы отправленные на обработку
     */
    @Bean
    public NewTopic documentsInTopic() {
        return new NewTopic(documentsIn, 1, (short) 1);
    }

    /**
     * Создает топик, из которого читаются документы после обработки
     */
    @Bean
    public NewTopic documentsOutTopic() {
        return new NewTopic(documentsOut, 1, (short) 1);
    }
}
