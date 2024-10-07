package net.wssouza.springboot;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any; // Importando especificamente o any do Mockito

import net.wssouza.springboot.controller.CategoriaControler;
import net.wssouza.springboot.entity.Categoria;
import net.wssouza.springboot.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.Collections;

public class CategoriaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaControler categoriaControler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaControler).build();
    }

    @Test
    public void testCriarCategoria() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setNome("Nova Categoria");

        when(categoriaService.salvarCategoria(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoria)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Nova Categoria"));
    }

    @Test
    public void testAtualizarCategoria() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setCategoriaId(1L);
        categoria.setNome("Categoria Atualizada");

        when(categoriaService.obterCategoriaPorId(1L)).thenReturn(Optional.of(categoria));
        when(categoriaService.salvarCategoria(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Categoria Atualizada"));
    }

    @Test
    public void testDeletarCategoria() throws Exception {
        doNothing().when(categoriaService).deletarCategoria(1L);

        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testObterCategoria() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setCategoriaId(1L);
        categoria.setNome("Categoria 1");

        when(categoriaService.obterCategoriaPorId(1L)).thenReturn(Optional.of(categoria));

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Categoria 1"));
    }

    @Test
    public void testObterCategoriaNotFound() throws Exception {
        when(categoriaService.obterCategoriaPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isNotFound());
    }
}