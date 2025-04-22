package sia.kafkaconsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(topics = "product-events", groupId = "product-events-listener")
    public void listenNewEmployee(String message) {
        LOGGER.info("Message Received: " + message);
    }

}