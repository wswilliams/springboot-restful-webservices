package net.wssouza.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    private Long orderId;
    @Column(nullable = false)
    private Double totalValue;

    @Column(nullable = false)
    private Date date;

    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @ManyToOne(optional = false)
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    public Order(Long orderId, Double totalValue, Date date) {
        this.orderId = orderId;
        this.totalValue = totalValue;
        this.date = date;
        this.products = new ArrayList<>();
    }
    public Product getProduct(Long productId) {
        return products.stream()
                .filter(o -> o.getProductId().equals(orderId))
                .findFirst()
                .orElse(null);
    }
    public void addProduct(Product product) {
        products.add(product);
    }
    public void addProducts(List<Product> products) {
        this.products.addAll(products);
    }
    // Somar os valores dos produtos
    public double getTotalValue() {
        return products.stream().mapToDouble(Product::getValue).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId) && Objects.equals(totalValue, order.totalValue) && Objects.equals(date, order.date) && Objects.equals(user, order.user) && Objects.equals(products, order.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, totalValue, date, user, products);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", totalValue=" + totalValue +
                ", date=" + date +
                ", user=" + user +
                ", products=" + products +
                '}';
    }
}
