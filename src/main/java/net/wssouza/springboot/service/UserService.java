package net.wssouza.springboot.service;

import jakarta.servlet.http.HttpServletRequest;
import net.wssouza.springboot.entity.User;

import java.util.List;

public interface UserService {

    public String signin(String username, String password);
    public String signup(User user);
    public void delete(String username);
    public User search(String username);
    public User whoami(HttpServletRequest req);
    public String refresh(String username);
    boolean existsByUsername(String username);
}
