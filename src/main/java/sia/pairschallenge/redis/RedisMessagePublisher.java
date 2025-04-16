package sia.pairschallenge.redis;

public interface RedisMessagePublisher {
    public void publish(String message);
}
