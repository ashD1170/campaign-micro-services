package com.ashish.in.campaign_management_service.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void sendEvent(String topic,Object event){
        kafkaTemplate.send(topic,event);
    }

}
