package com.bnifp.mufis.logservice.service.impl;

import com.bnifp.mufis.logservice.dto.input.LogInput;
import com.bnifp.mufis.logservice.service.LogService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import com.bnifp.mufis.logservice.service.KafkaConsumer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaConsumerImpl implements KafkaConsumer {
    private final static String topic = "fpbni-altera";
    private final static String groupId = "bni46Group";
    public static List<String> messages = new ArrayList<>();
    private final LogService logService;

    @KafkaListener(topics = topic, groupId = groupId)
    public void listen (String message){
        Gson g = new Gson();
        LogInput logInput = g.fromJson(message, LogInput.class);
        logService.create(logInput);
    }
}
