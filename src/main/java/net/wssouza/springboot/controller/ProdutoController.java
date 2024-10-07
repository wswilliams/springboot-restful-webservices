package net.wssouza.springboot.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.wssouza.springboot.entity.Categoria;
import net.wssouza.springboot.entity.Produto;
import net.wssouza.springboot.service.CategoriaService;
import net.wssouza.springboot.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.nio.file.StandardCopyOption;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/produtos")
@SecurityRequirement(name = "Bearer Authentication")  // JWT exigido
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CategoriaService categoriaService;


    @GetMapping
    public ResponseEntity<Page<Produto>> listarProdutos(Pageable pageable) {
        return ResponseEntity.ok(produtoService.listarProdutos(pageable));
    }

    @GetMapping("/sarch")
    public ResponseEntity<Page<Produto>> pesquisarProdutos(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "descricao", required = false) String descricao,
            Pageable pageable) {
        return ResponseEntity.ok(produtoService.pesquisarProdutos(nome, descricao, pageable));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Produto> criarProduto(
            @Parameter(description = "Nome do produto", required = true)
            @RequestParam("nome") String nome,

            @Parameter(description = "Descrição detalhada do produto", required = true)
            @RequestParam("descricao") String descricao,

            @Parameter(description = "Preço do produto", required = true)
            @RequestParam("preco") double preco,

            @Parameter(description = "Data de validade do produto no formato AAAA-MM-DD", required = true)
            @RequestParam("dataValidade") String dataValidade,

            @Parameter(description = "ID da categoria do produto", required = true)
            @RequestParam("categoriaId") Long categoriaId,

            @Parameter(description = "Imagem do produto", required = true)
            @RequestParam("imagem") MultipartFile imagem) throws IOException {

        if (!isImageFile(imagem)) {
            return ResponseEntity.badRequest().body(null);
        }

        String nomeArquivo = salvarImagem(imagem);

        LocalDate dateStartDate = LocalDate.parse(dataValidade);

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setDataValidade(dateStartDate);
        produto.setImagem(nomeArquivo);

        Optional<Categoria> categoria = categoriaService.obterCategoriaPorId(categoriaId);
        produto.setCategoria(categoria.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.salvarProduto(produto));
    }

    @PutMapping(value = "/{produtoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Produto> atualizarProduto(

            @Parameter(description = "Codigo do produto", required = true)
            @PathVariable ("produtoId") Long produtoId,

            @Parameter(description = "Nome do produto", required = true)
            @RequestParam("nome") String nome,

            @Parameter(description = "Descrição detalhada do produto", required = true)
            @RequestParam("descricao") String descricao,

            @Parameter(description = "Preço do produto", required = true)
            @RequestParam("preco") double preco,

            @Parameter(description = "Data de validade do produto no formato AAAA-MM-DD", required = true)
            @RequestParam("dataValidade") String dataValidade,

            @Parameter(description = "ID da categoria do produto", required = true)
            @RequestParam("categoriaId") Long categoriaId,

            @Parameter(description = "Imagem do produto", required = true)
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) throws IOException {

        Optional<Produto> produtoExistente = produtoService.obterProdutoPorId(produtoId);
        if (!produtoExistente.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Produto produto = produtoExistente.get();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);

        LocalDate dateStartDate = LocalDate.parse(dataValidade);

        produto.setDataValidade(dateStartDate);

        if (imagem != null && !imagem.isEmpty()) {
            if (!isImageFile(imagem)) {
                return ResponseEntity.badRequest().body(null);
            }

            deletarImagem(produto.getImagem());
            String nomeArquivo = salvarImagem(imagem);
            produto.setImagem(nomeArquivo);
        }

        Optional<Categoria> categoria = categoriaService.obterCategoriaPorId(categoriaId);
        produto.setCategoria(categoria.get());

        return ResponseEntity.ok(produtoService.salvarProduto(produto));
    }

    @DeleteMapping("/{produtoId}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long produtoId) {
        produtoService.deletarProduto(produtoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<Produto> obterProduto(@PathVariable Long produtoId) {
        Optional<Produto> produto = produtoService.obterProdutoPorId(produtoId);
        return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType.equals("image/png") || contentType.equals("image/jpeg");
    }

    private String salvarImagem(MultipartFile file) throws IOException {
        String nomeArquivo = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path caminhoArquivo = Paths.get("uploads/" + nomeArquivo);
        Files.copy(file.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);
        return nomeArquivo;
    }

    private void deletarImagem(String nomeArquivo) throws IOException {
        Path caminhoArquivo = Paths.get("uploads/" + nomeArquivo);
        Files.deleteIfExists(caminhoArquivo);
    }
}
