package net.wssouza.springboot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    private Long userId;
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;
    public User(Long userId, String name) {
        this.userId = userId;
        this.name = name;
        this.orders = new ArrayList<>();
    }
    public User() {}
    public void addOrder(Order order) {
        Order existingOrder = getOrder(order.getOrderId(), order.getDate());
        if (existingOrder != null) {
            existingOrder.addProducts(order.getProducts());
        } else {
            orders.add(order);
        }
    }
    public Order getOrder(Long orderId, Date date) {
        return orders.stream()
                .filter(o -> o.getOrderId().equals(orderId) && o.getDate().equals(date))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", orders=" + orders +
                '}';
    }
}
