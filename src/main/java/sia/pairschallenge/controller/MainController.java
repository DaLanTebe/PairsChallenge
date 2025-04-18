package sia.pairschallenge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
//import sia.pairschallenge.redis.RedisService;
import sia.pairschallenge.repository.Product;
//import sia.pairschallenge.redis.RedisService;
import sia.pairschallenge.service.impl.ProductServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class MainController {

    private final Logger log = LogManager.getLogger(MainController.class);


//    private final RedisService redisService;

    private final ProductServiceImpl productService;

    public MainController(ProductServiceImpl productService) {
        this.productService = productService;
//        this.redisService = redisService;
    }

    @PostMapping
    public ResponseEntity<String> createNewProduct(@RequestBody Product product) {
        productService.create(product);
        return ResponseEntity.ok("new product created");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProduct(@PathVariable String id) {
        Product productFromMainDB = productService.findById(Integer.parseInt(id));

        if (productFromMainDB != null){
            return ResponseEntity.ok(productFromMainDB.toString());
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
