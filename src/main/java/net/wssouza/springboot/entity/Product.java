package net.wssouza.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product  implements Serializable {

    @Id
    private Long productId;
    @Column(nullable = false)
    private Double value;

    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    private List<Order> orders;

    public Product(Long productId, double value) {
        this.productId = productId;
        this.value = value;
        this.orders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId) && Objects.equals(value, product.value) && Objects.equals(orders, product.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, value, orders);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", value=" + value +
                ", orders=" + orders +
                '}';
    }
}
