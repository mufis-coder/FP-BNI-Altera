package com.bnifp.mufis.authservice.service.impl;

import com.bnifp.mufis.authservice.service.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerImpl implements KafkaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("fpbni-altera")
    private String topic;

    public void produce (String message) {
        kafkaTemplate.send(topic, message);
    }
}
