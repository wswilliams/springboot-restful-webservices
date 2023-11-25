package net.wssouza.springboot.repository;

import net.wssouza.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    default List<User> findAllOrEmpty() {
        List<User> users = findAll();
        return users != null ? users : List.of();
    }
    @Query(value = "SELECT DISTINCT u.*, o.order_id,o.date,o.total_value, p.* "+
            "FROM "+
            "users u "+
            "LEFT JOIN "+
            "orders o "+
            "ON u.user_id = o.user_id "+
            "LEFT JOIN "+
            "order_product op "+
            "ON o.order_id = op.order_id "+
            "LEFT JOIN "+
            "products p "+
            "ON op.product_id = p.product_id "+
            "WHERE u.user_id = :userId", nativeQuery = true)
    List<User> findUserWithOrdersAndProducts(@Param("userId") Long userId);

    @Query(value = "SELECT DISTINCT u.*, o.order_id,o.date,o.total_value, p.* "+
            "FROM "+
            "users u "+
            "LEFT JOIN "+
            "orders o "+
            "ON u.user_id = o.user_id "+
            "LEFT JOIN "+
            "order_product op "+
            "ON o.order_id = op.order_id "+
            "LEFT JOIN "+
            "products p "+
            "ON op.product_id = p.product_id "+
            "WHERE u.user_id = :userId " +
            "AND o.date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<User> findUserWithOrdersAndProductsDate(@Param("userId") Long userId,
                                       @Param("startDate") Date startDate,
                                       @Param("endDate") Date endDate);
}
