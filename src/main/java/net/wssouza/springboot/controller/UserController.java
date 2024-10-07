package net.wssouza.springboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.wssouza.springboot.entity.dto.UserDataDTO;
import net.wssouza.springboot.entity.dto.UserResponseDTO;
import net.wssouza.springboot.entity.User;
import net.wssouza.springboot.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired(required=true)
    private ModelMapper modelMapper;

    @PostMapping("/login")
    @Operation(summary = "Sign in", description = "Autentica o usuário e retorna o token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Algo deu errado"),
            @ApiResponse(responseCode = "422", description = "Nome de usuário/senha inválidos fornecidos")
    })
    public String login(
            @Schema(description = "Username", required = true) @RequestParam String username,
            @Schema(description = "Password", required = true) @RequestParam String password) {
        return userService.signin(username, password);
    }

    @PostMapping("/cadastrar")
    @Operation(summary = "Sign up", description = "Cria um novo usuário e retorna token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Something went wrong"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "422", description = "O nome de usuário já está em uso")
    })
    public String signup(@Schema(description = "User signup data", required = true) @RequestBody UserDataDTO user) {
        return userService.signup(modelMapper.map(user, User.class));
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete user", description = "Deletes a specific user by username", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Something went wrong"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "O usuário não existe"),
            @ApiResponse(responseCode = "500", description = "Token JWT expirado ou inválido")
    })
    public String delete(@Schema(description = "Username", required = true) @PathVariable String username) {
        userService.delete(username);
        return username;
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Search user", description = "Returns specific user by username", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Something went wrong"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "O usuário não existe"),
            @ApiResponse(responseCode = "500", description = "Token JWT expirado ou inválido")
    })
    public UserResponseDTO search(@Schema(description = "Username", required = true) @PathVariable String username) {
        return modelMapper.map(userService.search(username), UserResponseDTO.class);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @Operation(summary = "Who am I", description = "Returns current user's data", security = @SecurityRequirement(name = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Something went wrong"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Token JWT expirado ou inválido")
    })
    public UserResponseDTO whoami(HttpServletRequest req) {
        return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @Operation(summary = "Refresh token", description = "Atualiza o token JWT para o usuário atual")
    public String refresh(HttpServletRequest req) {
        return userService.refresh(req.getRemoteUser());
    }
}