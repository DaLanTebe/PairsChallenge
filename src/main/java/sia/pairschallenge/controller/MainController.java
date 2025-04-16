package sia.pairschallenge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import sia.pairschallenge.repository.Product;
import sia.pairschallenge.service.impl.ProductServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class MainController {

    private final Logger log = LogManager.getLogger(MainController.class);

    private final KafkaTemplate<String, Product> kafkaTemplate;

    private final ProductServiceImpl productService;

    String kafkaConsumer = "";

    public MainController(KafkaTemplate<String, Product> kafkaTemplate, ProductServiceImpl productService) {
        this.kafkaTemplate = kafkaTemplate;
        this.productService = productService;
    }

    @PostMapping
    public void createNewProduct() {
        kafkaConsumer = "get message";
        productService.create(new Product());
    }

    @GetMapping("/{id}")
    public void getProduct(Product product) {
        String message = "";

        try {
            message = new ObjectMapper().writeValueAsString(product);
        } catch (JsonProcessingException e) {
            log.error("Unable to cast object to json" + e);
        }

        kafkaTemplate.send("product-events", new Product());
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

        kafkaTemplate.send("product-events", new Product());
    }

    @PutMapping
    public void updateProduct() {
        kafkaConsumer = "get message";
        productService.update(new Product());
    }

    @DeleteMapping("/{id}")
    public void deleteProduct() {
        kafkaConsumer = "delete message";
        productService.deleteById(6);
    }

}
