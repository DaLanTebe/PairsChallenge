package sia.pairschallenge.service.event;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sia.pairschallenge.repository.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductEvent implements Serializable{

        private int id;

        private String name;

        private String description;

        private BigDecimal price;

        private Integer quantity;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;

        private String message;

        public static ProductEvent from(Product product, String message) {
                return new ProductEvent(message, product);
        }

        private ProductEvent(String message, Product product) {
                id = product.getId();
                name = product.getName();
                description = product.getDescription();
                price = product.getPrice();
                quantity = product.getQuantity();
                createdAt = product.getCreatedAt();
                updatedAt = product.getUpdatedAt();
                this.message = message;
        }

        public int getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public String getDescription() {
                return description;
        }

        public BigDecimal getPrice() {
                return price;
        }

        public Integer getQuantity() {
                return quantity;
        }

        public LocalDateTime getCreatedAt() {
                return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
                return updatedAt;
        }

        public String getMessage() {
                return message;
        }

        public void setId(int id) {
                this.id = id;
        }

        public void setName(String name) {
                this.name = name;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public void setPrice(BigDecimal price) {
                this.price = price;
        }

        public void setQuantity(Integer quantity) {
                this.quantity = quantity;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
                this.updatedAt = updatedAt;
        }

        public void setMessage(String message) {
                this.message = message;
        }
}
