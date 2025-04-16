package sia.pairschallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MySimpleRepository extends JpaRepository<Product, Integer> {
}
