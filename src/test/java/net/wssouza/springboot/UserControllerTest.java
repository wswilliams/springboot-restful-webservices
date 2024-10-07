
package net.wssouza.springboot;
import net.wssouza.springboot.controller.UserController;
import net.wssouza.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Disabled;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import net.wssouza.springboot.entity.User;

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

    @InjectMocks
    private UserController userController;

    @Disabled
    @Test
    public void testUploadFile() {
        // Criar um exemplo de arquivo multipart

    }

    @Disabled
    @Test
    public void testGetUserById() {


    }

//    @Disabled
    @Test
    public void testSearch() {
        // Configurar o comportamento esperado do UserService


    }
//    @Disabled
    @Test
    public void testGetAllUsers() {

    }
}
