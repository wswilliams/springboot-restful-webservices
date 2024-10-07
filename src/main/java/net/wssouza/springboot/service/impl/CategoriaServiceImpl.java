package net.wssouza.springboot.service.impl;

import lombok.AllArgsConstructor;
import net.wssouza.springboot.entity.Categoria;
import net.wssouza.springboot.repository.CategoriaRepository;
import net.wssouza.springboot.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {


    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public Page<Categoria> listarCategorias(String nome, Pageable pageable) {

        return categoriaRepository.buscarPorNome(nome, pageable);
    }

    @Override
    public Categoria salvarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Optional<Categoria> obterCategoriaPorId(Long categoriaId) {
        return categoriaRepository.findById(categoriaId);
    }

    @Override
    public void deletarCategoria(Long categoriaId) {
        categoriaRepository.deleteById(categoriaId);
    }
}
