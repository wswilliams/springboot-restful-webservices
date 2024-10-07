package net.wssouza.springboot.service;

import net.wssouza.springboot.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProdutoService {

    public Page<Produto> listarProdutos(Pageable pageable);

    public Page<Produto> pesquisarProdutos(String nome, String descricao, Pageable pageable);

    public Produto salvarProduto(Produto produto);

    public Optional<Produto> obterProdutoPorId(Long produtoId);

    public void deletarProduto(Long produtoId);
}
