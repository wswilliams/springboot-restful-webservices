package net.wssouza.springboot.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.wssouza.springboot.entity.User;
import net.wssouza.springboot.entity.Order;
import net.wssouza.springboot.entity.Product;
import net.wssouza.springboot.repository.OrderRepository;
import net.wssouza.springboot.repository.ProductRepository;
import net.wssouza.springboot.repository.UserRepository;
import net.wssouza.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    @Autowired
    private CacheManager cacheManager;

    @Transactional
    public void createUser(List<User> users) {

        for (User user : users) {
            // Salve o usuário
            User _user = new User(user.getUserId(), user.getName());
            userRepository.save(_user);

            for (Order order : user.getOrders()) {

                Order _order =  new Order(order.getOrderId(),order.getTotalValue(), order.getDate());
                _order.setUser(_user);
                orderRepository.save(_order);

                // Associe o pedido aos produtos
                for (Product product : order.getProducts()) {
                    Product _product = new Product(product.getProductId(), product.getValue());
                    _product.addOrder(_order);
                    _order.addProduct(_product);
                   productRepository.save(_product);
                    orderRepository.insertOrderProductRelation(_order.getOrderId(), _product.getProductId());
                }
                _user.addOrder(_order);
            }
        }
    }

   @Cacheable(value = "userCache", key = "{#userId, #startDate, #endDate}")
//    @Cacheable(key = "{#userId, #startDate, #endDate}", unless = "#result == null")
    @Override
    public List<User> findUserWithOrdersAndProductsDate(Long userId, String startDate, String endDate) {

        Cache userCache = cacheManager.getCache("userCache");
        Cache.ValueWrapper valueWrapper = userCache.get(userId + "_" + startDate + "_" + endDate);
        if (valueWrapper != null) {
            // Result is in the cache, return it directly
            return (List<User>) valueWrapper.get();
        }

        try {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            Date dateStartDate = dateFormat.parse(startDate);
            Date dateEndDate = dateFormat.parse(endDate);
            List<User> result = userRepository.findUserWithOrdersAndProductsDate(userId, dateStartDate, dateEndDate);
            // Cache the result
            User _user = newUser(result.get(0));
            userCache.put(userId + "_" + startDate + "_" + endDate, _user);

            List<User> _result =  new ArrayList<>();
            _result.add(result.get(0));
            return  _result;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User newUser(User user) {
        User _user = new User(user.getUserId(), user.getName());

        for (Order order : user.getOrders()) {
            Order _order = new Order(order.getOrderId(), order.getTotalValue(), order.getDate());
            _order.setUser(_user);

            // Associe o pedido aos produtos
            for (Product product : order.getProducts()) {
                Product _product = new Product(product.getProductId(), product.getValue());
                _product.addOrder(_order);
                _order.addProduct(_product);
            }
            _user.addOrder(_order);
        }
        return _user;
    }

    @Cacheable(value = "userCache", key = "{#userId}")
    @Override
    public List<User> findUserWithOrdersAndProducts(Long userId) {
        Cache userCache = cacheManager.getCache("userCache");
        Cache.ValueWrapper valueWrapper = userCache.get(userId);
        if (valueWrapper != null) {
            // Result is in the cache, return it directly
            return (List<User>) valueWrapper.get();
        }
        List<User> result = userRepository.findUserWithOrdersAndProducts(userId);
        User _user = newUser(result.get(0));
        userCache.put(userId, _user);
        List<User> _result =  new ArrayList<>();
        _result.add(result.get(0));
        return  _result;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllOrEmpty();
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
