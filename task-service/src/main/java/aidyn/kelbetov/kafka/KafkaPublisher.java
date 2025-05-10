package aidyn.kelbetov.kafka;

import aidyn.kelbetov.DTO.TaskEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaPublisher {
    @Autowired
    private KafkaTemplate<String, TaskEventDto> kafkaTemplate;

    public void sendMessage(TaskEventDto eventDto){
        kafkaTemplate.send("task_notifications", eventDto);
    }
}
