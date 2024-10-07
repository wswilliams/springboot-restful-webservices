package net.wssouza.springboot.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import net.wssouza.springboot.entity.Categoria;
import net.wssouza.springboot.entity.User;
import net.wssouza.springboot.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/categorias")
@SecurityRequirement(name = "Bearer Authentication")  // JWT exigido
public class CategoriaControler {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<Page<Categoria>> listarCategorias(
            @RequestParam(value = "nome", required = false) String nome,
            Pageable pageable) {
        return ResponseEntity.ok(categoriaService.listarCategorias(nome, pageable));
    }

    @PostMapping
    public ResponseEntity<Categoria> criarProduto(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.salvarCategoria(categoria));
    }

    @PutMapping("/{categoriaId}")
    public ResponseEntity<Categoria> atualizarCategoria(@PathVariable Long categoriaId, @Valid @RequestBody Categoria categoria) {
        Optional<Categoria> categoriaExistente = categoriaService.obterCategoriaPorId(categoriaId);
        if (!categoriaExistente.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        categoria.setCategoriaId(categoriaId);
        return ResponseEntity.ok(categoriaService.salvarCategoria(categoria));
    }

    @DeleteMapping("/{categoriaId}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long categoriaId) {
        categoriaService.deletarCategoria(categoriaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{categoriaId}")
    public ResponseEntity<Categoria> obterCategoria(@PathVariable Long categoriaId) {
        Optional<Categoria> categoria = categoriaService.obterCategoriaPorId(categoriaId);
        return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
