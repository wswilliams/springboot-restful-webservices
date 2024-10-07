package net.wssouza.springboot.entity.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import net.wssouza.springboot.entity.Role;

public class UserDataDTO {

  @Schema(description = "Nome de usuário", example = "john_doe", required = true)
  private String username;

  @Schema(description = "Email do usuário", example = "john.doe@example.com", required = true)
  private String email;

  @Schema(description = "Senha do usuário", example = "password123", required = true)
  private String password;

  @Schema(description = "Lista de papéis/roles atribuídos ao usuário", required = true)
  private List<Role> roles;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

}