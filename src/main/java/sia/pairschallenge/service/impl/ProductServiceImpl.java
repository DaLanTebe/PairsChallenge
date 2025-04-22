package sia.pairschallenge.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import sia.pairschallenge.repository.Product;
import sia.pairschallenge.repository.ProductRepository;
import sia.pairschallenge.service.ProductService;
import sia.pairschallenge.service.event.ProductEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@CacheConfig(cacheNames = "productCache")
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LogManager.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, KafkaTemplate<String, ProductEvent> kafkaTemplate) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @CachePut(key = "#product.id")
    public void update(Integer id, Product product) {
        Product productFromMainDB = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));

            product.setId(id);
            product.setCreatedAt(productFromMainDB.getCreatedAt());

            Product savedProduct = productRepository.save(product);

            CompletableFuture<SendResult<String, ProductEvent>> send =
                    kafkaTemplate.send("product-events", ProductEvent.from(savedProduct, "product updated"));
            send.whenComplete((result, exception) -> {
                if (exception != null) {
                    log.error("message failed to send", exception);
                }
            });
    }

    @Override
    @Cacheable(key = "#id")
    public Product findById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Integer id) {
        Product productForDelete = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));

            productRepository.deleteById(id);

            CompletableFuture<SendResult<String, ProductEvent>> send =
                    kafkaTemplate.send("product-events", ProductEvent.from(productForDelete, "this product has deleted"));
            send.whenComplete((result, exception) -> {
                if (exception != null) {
                    log.error("message failed to send", exception);
                }
            });
    }

    @Override
    public void create(Product product) {
        productRepository.save(product);
        CompletableFuture<SendResult<String, ProductEvent>> send =
                kafkaTemplate.send("product-events", ProductEvent.from(product, "product created"));
        send.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("message failed to send", exception);
            }
        });

    }

    @Override
    public List<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).getContent();
    }
}
