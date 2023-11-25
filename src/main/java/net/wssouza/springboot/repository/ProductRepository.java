package net.wssouza.springboot.repository;

import net.wssouza.springboot.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product, Long> {
}
