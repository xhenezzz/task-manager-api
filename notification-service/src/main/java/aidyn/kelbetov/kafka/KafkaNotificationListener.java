package aidyn.kelbetov.kafka;

import aidyn.kelbetov.DTO.TaskEventDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaNotificationListener {

    @KafkaListener(topics = "task_notifications", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(TaskEventDto event) {
        System.out.println("🔔 Уведомление: Задача '" + event.getTitle() + "' для пользователя " + event.getUserId());
    }
}
