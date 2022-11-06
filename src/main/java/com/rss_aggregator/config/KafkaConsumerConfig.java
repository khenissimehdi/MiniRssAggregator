package com.rss_aggregator.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rss_aggregator.entity.Article;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import rssfeedscraper.Answer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Bean
    public ConsumerFactory<String, List<Answer>> consumerFactory(){
        Map<String,Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
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
