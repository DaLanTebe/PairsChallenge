package sia.pairschallenge.redis;

import org.springframework.data.repository.CrudRepository;
import sia.pairschallenge.repository.Product;

public interface RedisRepository{
    Product findById(String id);
    public void save(Product product);
    public void delete(Product product);
}
