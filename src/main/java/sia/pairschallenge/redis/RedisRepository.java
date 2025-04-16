package sia.pairschallenge.redis;

import sia.pairschallenge.repository.Product;

public interface RedisRepository {

    Product findProductById(String id);
    void addProduct(Product product);
    void deleteProduct(String id);
}
