package sia.pairschallenge.redis.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import sia.pairschallenge.redis.RedisMessagePublisher;
import sia.pairschallenge.repository.Product;

@Service
public class RedisMessagePublisherImpl implements RedisMessagePublisher {

    private RedisTemplate<String, Product> redisTemplate;

    private ChannelTopic channelTopic;

    @Autowired
    public RedisMessagePublisherImpl(final RedisTemplate<String, Product> redisTemplate,final ChannelTopic channelTopic) {
        this.redisTemplate = redisTemplate;
        this.channelTopic = channelTopic;
    }

    public RedisMessagePublisherImpl() {}

    @Override
    public void publish(String message) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }
}
