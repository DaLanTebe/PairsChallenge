package sia.pairschallenge.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sia.pairschallenge.repository.Product;
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
    public ResponseEntity<String> createNewProduct(@RequestBody Product product) {
        productService.create(product);
        return ResponseEntity.ok("Product created");
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
    public ResponseEntity<String> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        productService.update(id, product);
        return ResponseEntity.ok("Product updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct() {
        productService.deleteById(6);
        return ResponseEntity.ok("Product deleted");
    }

}
