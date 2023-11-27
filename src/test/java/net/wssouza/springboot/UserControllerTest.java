
package net.wssouza.springboot;
import net.wssouza.springboot.controller.UserController;
import net.wssouza.springboot.service.FilesStorageService;
import net.wssouza.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Disabled;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import net.wssouza.springboot.entity.User;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Disabled
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private FilesStorageService storageService;

    @InjectMocks
    private UserController userController;

    @Disabled
    @Test
    public void testUploadFile() {
        // Criar um exemplo de arquivo multipart
        MultipartFile mockFile = new MockMultipartFile(
                "file",
                "data_1.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "0000000070                              Palmer Prosacco00000007530000000003     1836.7420210308".getBytes()
        );

                // Configurar o comportamento esperado do FilesStorageService
                when(storageService.store(any(MultipartFile.class))).thenReturn("data_1.txt");
        when(storageService.openFile(anyString())).thenReturn(List.of(new User()));

        // Executar o método sob teste
        ResponseEntity<List<User>> responseEntity = userController.uploadFile(mockFile);

        // Verificar se o resultado é o esperado
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Disabled
    @Test
    public void testGetUserById() {
        // Configurar o comportamento esperado do UserService
        when(userService.findUserWithOrdersAndProducts(anyLong())).thenReturn(List.of(new User()));

        // Executar o método sob teste
        ResponseEntity<List<User>> responseEntity = userController.getUserById(1L);

        // Verificar se o resultado é o esperado
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

//    @Disabled
    @Test
    public void testSearch() {
        // Configurar o comportamento esperado do UserService
        when(userService.findUserWithOrdersAndProductsDate(anyLong(), anyString(), anyString())).thenReturn(List.of(new User()));

        // Executar o método sob teste
        ResponseEntity<List<User>> responseEntity = userController.search(2L, "2021-07-14", "2021-12-12");

        // Verificar se o resultado é o esperado
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }
//    @Disabled
    @Test
    public void testGetAllUsers() {
        // Configurar o comportamento esperado do UserService
        when(userService.getAllUsers()).thenReturn(List.of(new User()));

        // Executar o método sob teste
        ResponseEntity<List<User>> responseEntity = userController.getAllUsers();

        // Verificar se o resultado é o esperado
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }
}
