package net.wssouza.springboot;

import net.wssouza.springboot.controller.ProdutoController;
import net.wssouza.springboot.entity.Categoria;
import net.wssouza.springboot.entity.Produto;
import net.wssouza.springboot.service.CategoriaService;
import net.wssouza.springboot.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private ProdutoController produtoController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController).build();
    }

    @Test
    public void testCriarProduto() throws Exception {
        MockMultipartFile imagem = new MockMultipartFile("imagem", "imagem.jpg", MediaType.IMAGE_JPEG_VALUE, "imagem".getBytes());

        Produto produto = new Produto();
        produto.setNome("Produto 1");

        when(categoriaService.obterCategoriaPorId(anyLong())).thenReturn(Optional.of(new Categoria()));
        when(produtoService.salvarProduto(any(Produto.class))).thenReturn(produto);

        mockMvc.perform(multipart("/api/produtos")
                        .file(imagem)
                        .param("nome", "Produto 1")
                        .param("descricao", "Descrição do Produto 1")
                        .param("preco", "10.0")
                        .param("dataValidade", LocalDate.now().toString())
                        .param("categoriaId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Produto 1"));
    }


    @Test
    public void testDeletarProduto() throws Exception {
        Long produtoId = 1L;
        doNothing().when(produtoService).deletarProduto(produtoId);

        mockMvc.perform(delete("/api/produtos/{produtoId}", produtoId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testObterProduto() throws Exception {
        Long produtoId = 1L;
        Produto produto = new Produto();
        produto.setNome("Produto 1");

        when(produtoService.obterProdutoPorId(produtoId)).thenReturn(Optional.of(produto));

        mockMvc.perform(get("/api/produtos/{produtoId}", produtoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Produto 1"));
    }

    @Test
    public void testObterProdutoNotFound() throws Exception {
        Long produtoId = 1L;
        when(produtoService.obterProdutoPorId(produtoId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/produtos/{produtoId}", produtoId))
                .andExpect(status().isNotFound());
    }
}
