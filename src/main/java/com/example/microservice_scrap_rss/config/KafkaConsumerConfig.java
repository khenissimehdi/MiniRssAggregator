package com.example.microservice_scrap_rss.config;

import com.example.microservice_scrap_rss.rssfeedscraper.Answer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String serverValue;
    @Bean
    public ConsumerFactory<String, List<Answer>> consumerFactory(){
        Map<String,Object> config = new HashMap<>();
        config.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, 104857600);
        config.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 104857600);
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,serverValue);
        config.put(ConsumerConfig.GROUP_ID_CONFIG,"foo");
        ObjectMapper om = new ObjectMapper();
        JavaType type = om.getTypeFactory().constructParametricType(List.class, Answer.class);
        return new DefaultKafkaConsumerFactory<>(config,new StringDeserializer(), new JsonDeserializer<>(type, om, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, List<Answer>> fooListener(){
        ConcurrentKafkaListenerContainerFactory<String, List<Answer>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
