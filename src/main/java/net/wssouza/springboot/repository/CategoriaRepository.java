package net.wssouza.springboot.repository;

import net.wssouza.springboot.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>,  CategoriaRepositoryCustom {

}