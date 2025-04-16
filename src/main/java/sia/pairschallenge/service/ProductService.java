package sia.pairschallenge.service;

import sia.pairschallenge.repository.Product;

import java.util.List;

public interface ProductService{

    void update(Product product);

    Product findById(Integer id);

    void deleteById(Integer id);

    void create(Product product);

    List<Product> findAll();
}
