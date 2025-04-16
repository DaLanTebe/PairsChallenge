package sia.pairschallenge.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageSubscriber implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(MessageSubscriber.class);
    public static List<String> messageList = new ArrayList<>();

    @Override
    public void onMessage(final Message message, byte[] pattern) {
        messageList.add(message.toString());
        log.info("Message received: {}", message.toString());
    }
}
