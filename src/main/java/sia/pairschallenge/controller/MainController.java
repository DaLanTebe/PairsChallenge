package sia.pairschallenge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import sia.pairschallenge.redis.RedisRepository;
import sia.pairschallenge.repository.Product;
import sia.pairschallenge.service.impl.ProductServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class MainController {

    private final Logger log = LogManager.getLogger(MainController.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final RedisRepository redisRepository;

    private final ProductServiceImpl productService;

    public MainController(KafkaTemplate<String, String> kafkaTemplate, ProductServiceImpl productService, RedisRepository redisRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.productService = productService;
        this.redisRepository = redisRepository;
    }

    @PostMapping
    public ResponseEntity<String> createNewProduct(@RequestBody Product product) {
        productService.create(product);
        kafkaTemplate.send("product-events", product.toString() + " created");
        return ResponseEntity.ok("new product created");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        Product cashedProduct = redisRepository.findById(id);
        if (cashedProduct != null){
            System.out.println("cashed");
            return ResponseEntity.ok(cashedProduct);
        }

        Product productFromMainDB = productService.findById(Integer.parseInt(id));
        if (productFromMainDB != null){
            redisRepository.save(productFromMainDB);
            System.out.println("main bd");
            return ResponseEntity.ok(productFromMainDB);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public void getProducts() {
        String message = "";
        try {
            List<Product> allProducts = productService.findAll();
            message = new ObjectMapper().writeValueAsString(allProducts);
        } catch (JsonProcessingException e) {
            log.error("Unable to cast object to json" + e);
        }

        kafkaTemplate.send("product-events", "ded");
    }

    @PutMapping
    public void updateProduct() {
        productService.update(new Product());
    }

    @DeleteMapping("/{id}")
    public void deleteProduct() {
        productService.deleteById(6);
    }

}
