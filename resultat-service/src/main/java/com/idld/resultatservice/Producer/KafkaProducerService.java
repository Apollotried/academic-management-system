package com.idld.resultatservice.Producer;


import com.idld.resultatservice.entities.GradeNotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//@Service
//public class KafkaProducerService {
  //  private static final String TOPIC = "grades-topic";

    //@Autowired
    //private KafkaTemplate<String, String> kafkaTemplate;

    //public void sendMessage(String message) {
      //  kafkaTemplate.send(TOPIC, message);
    //}
//}
    @Service
    public class KafkaProducerService {
        private final KafkaTemplate<String, GradeNotificationEvent> kafkaTemplate;

        public KafkaProducerService(KafkaTemplate<String, GradeNotificationEvent> kafkaTemplate) {
            this.kafkaTemplate = kafkaTemplate;
        }

        public void sendMessage(GradeNotificationEvent gradeEvent) {
            kafkaTemplate.send("grades-notifications", gradeEvent);
        }
    }

