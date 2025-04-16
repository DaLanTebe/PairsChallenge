package sia.pairschallenge.redis.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import sia.pairschallenge.redis.RedisRepository;
import sia.pairschallenge.repository.Product;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

    private static final String KEY = "products";
    private RedisTemplate<String, Product> redisTemplate;
    private HashOperations<String, String, Product> hashOperations;

    @Autowired
    public RedisRepositoryImpl(RedisTemplate<String, Product> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Product findProductById(String id) {
        return (Product) hashOperations.get(KEY, id);
    }

    @Override
    public void addProduct(final Product product) {
        hashOperations.put(KEY, String.valueOf(product.getId()), product);
    }

    @Override
    public void deleteProduct(String id) {
        hashOperations.delete(KEY, id);
    }
}
