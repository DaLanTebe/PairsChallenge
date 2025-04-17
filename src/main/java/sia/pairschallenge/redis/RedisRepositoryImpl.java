package sia.pairschallenge.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import sia.pairschallenge.repository.Product;

@Repository
public class RedisRepositoryImpl implements RedisRepository {
    private static final String PRODUCTS_KEY = "products";

    private RedisTemplate<String, Product> redisTemplate;

    public RedisRepositoryImpl(RedisTemplate<String, Product> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Product findById(String id) {
        return redisTemplate.opsForValue().get(id);
    }

    @Override
    public void save(Product product) {
        redisTemplate.opsForValue().set(String.valueOf(product.getId()), product);
    }

    @Override
    public void delete(Product product) {
        redisTemplate.delete(String.valueOf(product.getId()));
    }
}
