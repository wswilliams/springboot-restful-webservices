package net.wssouza.springboot.repository;

import net.wssouza.springboot.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoriaRepositoryCustom {
    Page<Categoria> buscarPorNome(String nome, Pageable pageable);
}
