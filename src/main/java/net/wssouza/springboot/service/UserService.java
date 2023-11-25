package net.wssouza.springboot.service;

import net.wssouza.springboot.entity.User;

import java.util.Date;
import java.util.List;

public interface UserService {
    void createUser(List<User> users);

    public List<User> findUserWithOrdersAndProductsDate(Long userId, String startDate, String endDate);
    public List<User> findUserWithOrdersAndProducts(Long userId);

    public List<User> getAllUsers();

    public User newUser(User user) ;

    void deleteUser(Long userId);
}
