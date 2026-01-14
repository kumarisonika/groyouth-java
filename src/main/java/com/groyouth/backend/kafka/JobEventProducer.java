package com.groyouth.backend.kafka;

import com.groyouth.backend.event.JobAppliedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobEventProducer {
    private final KafkaTemplate<String, JobAppliedEvent> kafkaTemplate;

    public JobEventProducer(KafkaTemplate<String, JobAppliedEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishJobApplied(JobAppliedEvent event){
        kafkaTemplate.send("job-applied", event);
    }
}
