package net.wssouza.springboot.repository;

import net.wssouza.springboot.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoRepositoryCustom {
    Page<Produto> listarProdutos(Pageable pageable);
    Page<Produto> buscarPorNomeOrDescricao(String nome, String descricao, Pageable pageable);
}
