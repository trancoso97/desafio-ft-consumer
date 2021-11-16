package com.desafio.ftrack.orderconsumer.configurations;


import java.util.HashMap;
import java.util.Map;

import com.desafio.ftrack.orderconsumer.types.Order;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConfig {
    
    @Value(value = "${app.kafka.host}")
    private String kafkaHost;

    @Value(value = "${app.kafka.topic}")
    private String topicName;

    @Value(value = "${app.kafka.groupid}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, Order> orderConsumerFactory(){
        Map<String,Object> conf = new HashMap<>();
        conf.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);
        conf.put(ConsumerConfig.GROUP_ID_CONFIG, StringSerializer.class);
        conf.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        conf.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<Order> deserializer = new JsonDeserializer<>(Order.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaConsumerFactory<>(conf, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,Order> orderListener(){
        ConcurrentKafkaListenerContainerFactory<String,Order> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory());
        factory.getContainerProperties().setIdleBetweenPolls(1000);
        return factory;
    }


}
