package sia.pairschallenge.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import sia.pairschallenge.redis.impl.RedisMessagePublisherImpl;
import sia.pairschallenge.repository.Product;

@Configuration
public class RedisConfig {
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Product> redisTemplate(RedisConnectionFactory connectionFactory) {
        final RedisTemplate<String, Product> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(new GenericToStringSerializer<Product>(Product.class));
        return template;
    }

    @Bean
    MessageSubscriber messageSubscriber() {
        return new MessageSubscriber();
    }

//    @Bean
//    RedisMessageListenerContainer redisContainer() {
//        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(jedisConnectionFactory());
//        container.addMessageListener(messageListener(), channelTopic());
//        return container;
//    }
//
//    @Bean
//    MessageListener messageListener() {
//        return new MessageListenerAdapter(messageSubscriber());
//    }

    @Bean
    ChannelTopic channelTopic() {
        return new ChannelTopic("pubsub:queue");
    }

    @Bean
    RedisMessagePublisherImpl redisMessagePublisher() {
        return new RedisMessagePublisherImpl(redisTemplate(jedisConnectionFactory()), channelTopic());
    }
}
