package net.wssouza.springboot.service.impl;

import lombok.AllArgsConstructor;
import net.wssouza.springboot.entity.Produto;
import net.wssouza.springboot.repository.ProdutoRepository;
import net.wssouza.springboot.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public Page<Produto> listarProdutos(Pageable pageable) {

        return produtoRepository.listarProdutos (pageable);
    }

    @Override
    public Page<Produto> pesquisarProdutos(String nome, String descricao, Pageable pageable) {
        return produtoRepository.buscarPorNomeOrDescricao (nome, descricao, pageable);
    }

    @Override
    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Override
    public Optional<Produto> obterProdutoPorId(Long produtoId) {
        return produtoRepository.findById(produtoId);
    }

    @Override
    public void deletarProduto(Long produtoId) {
        produtoRepository.deleteById(produtoId);
    }
}
