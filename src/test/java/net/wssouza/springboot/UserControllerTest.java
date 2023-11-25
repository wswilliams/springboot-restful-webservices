package net.wssouza.springboot;

import net.wssouza.springboot.controller.UserController;
import net.wssouza.springboot.entity.User;
import net.wssouza.springboot.service.FilesStorageService;
import net.wssouza.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private FilesStorageService storageService;

    @InjectMocks
    private UserController userController;

    @Test
    static void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "data_1.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        when(storageService.store(any())).thenReturn("data_1.txt");
        when(storageService.openFile("data_1.txt")).thenReturn(Collections.singletonList(new User()));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users/upload")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    static void testGetUserById() throws Exception {
        Long userId = 1L;
        when(userService.findUserWithOrdersAndProducts(userId)).thenReturn(Collections.singletonList(new User()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    static void testSearch() throws Exception {
        Long userId = 2L;
        String startDate = "2021-07-14";
        String endDate = "2021-12-12";

        when(userService.findUserWithOrdersAndProductsDate(userId, startDate, endDate))
                .thenReturn(Collections.singletonList(new User()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/search/{userId}/{startDate}/{endDate}", userId, startDate, endDate))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    static void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(new User()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
