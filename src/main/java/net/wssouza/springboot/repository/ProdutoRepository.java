package net.wssouza.springboot.repository;

import net.wssouza.springboot.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> , ProdutoRepositoryCustom{
}
