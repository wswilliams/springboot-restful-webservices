
package net.wssouza.springboot;

import jakarta.servlet.http.HttpServletRequest;
import net.wssouza.springboot.controller.UserController;
import net.wssouza.springboot.entity.User;
import net.wssouza.springboot.entity.dto.UserDataDTO;
import net.wssouza.springboot.entity.dto.UserResponseDTO;
import net.wssouza.springboot.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testLogin() throws Exception {
        String username = "testuser";
        String password = "testpass";
        String token = "jwtToken";

        when(userService.signin(username, password)).thenReturn(token);

        mockMvc.perform(post("/users/login")
                        .param("username", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUser() throws Exception {
        String username = "testuser";

        doNothing().when(userService).delete(username);

        mockMvc.perform(delete("/users/{username}", username))
                .andExpect(status().isOk())
                .andExpect(content().string(username));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSearchUser() throws Exception {
        String username = "testuser";
        User user = new User();
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        when(userService.search(username)).thenReturn(user);
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);

        mockMvc.perform(get("/users/{username}", username))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "CLIENT"})
    public void testWhoami() throws Exception {
        HttpServletRequest request = null;
        User user = new User();
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        when(userService.whoami(request)).thenReturn(user);
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk());
    }

}