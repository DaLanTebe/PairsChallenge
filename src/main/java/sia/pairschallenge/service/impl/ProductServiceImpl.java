package sia.pairschallenge.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
    public void update(Product product) {
        productRepository.save(product);
    }

    @Override
    @Cacheable(value = "productCache", key = "#id")
    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public void create(Product product) {
        productRepository.save(product);
//        CompletableFuture<SendResult<String, ProductEvent>> send = kafkaTemplate.send("product-events", ProductEvent.from(product, "product created"));
//        send.whenComplete((result, exception) -> {
//            if (exception != null) {
//                log.error("message failed to send", exception);
//            }
//        });

    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
