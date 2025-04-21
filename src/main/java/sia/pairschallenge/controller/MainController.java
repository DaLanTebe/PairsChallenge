package sia.pairschallenge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
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

    private final ProductServiceImpl productService;

    public MainController(ProductServiceImpl productService) {
        this.productService = productService;

    }

    @PostMapping
    public void createNewProduct(@RequestBody Product product) {
        productService.create(product);
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
    public ResponseEntity<String> getProducts(@RequestParam() Integer page, @RequestParam Integer size) {
        List<Product> allProducts = productService.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(allProducts.toString());
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        productService.update(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct() {
        productService.deleteById(6);
    }

}
