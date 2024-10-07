package net.wssouza.springboot.service;

import net.wssouza.springboot.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {

    public Page<Categoria> listarCategorias(String nome, Pageable pageable);

    public Categoria salvarCategoria(Categoria categoria);

    public Optional<Categoria> obterCategoriaPorId(Long categoriaId);

    public void deletarCategoria(Long categoriaId);

}
