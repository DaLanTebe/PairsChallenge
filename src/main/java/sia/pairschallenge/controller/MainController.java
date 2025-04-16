package sia.pairschallenge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import sia.pairschallenge.repository.Product;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class MainController {

    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public MainController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public void createNewProduct() {
        //save product
        kafkaTemplate.send("product-events", "created new product");
    }

    @GetMapping("/{id}")
    public void getProduct(Product product) {
        String message = "";

        try {
            message = new ObjectMapper().writeValueAsString(product);
        } catch (JsonProcessingException e) {
        }

        kafkaTemplate.send("product-events", message);
    }

    @GetMapping
    public void getProducts(List<Product> products) {
        String message = "";

        try {
            message = new ObjectMapper().writeValueAsString(products);
        } catch (JsonProcessingException e) {
        }

        kafkaTemplate.send("product-events", message);
    }

    @PutMapping
    public void updateProduct() {}

    @DeleteMapping("/{id}")
    public void deleteProduct() {}

}
