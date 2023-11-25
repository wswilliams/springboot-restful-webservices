package net.wssouza.springboot.repository;

//
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import net.wssouza.springboot.entity.Order;


public interface OrderRepository extends CrudRepository<Order, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO order_product (order_id, product_id) VALUES (:orderId, :productId)", nativeQuery = true)
    void insertOrderProductRelation(Long orderId, Long productId);
}
