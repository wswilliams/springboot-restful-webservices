package net.wssouza.springboot.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import net.wssouza.springboot.entity.User;
import net.wssouza.springboot.service.FilesStorageService;
import net.wssouza.springboot.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private UserService userService;

    @Autowired
    FilesStorageService storageService;

    // build create User with upload the file REST API
    // http://localhost:8080/api/users/upload
    @RequestMapping(value="/upload", produces = { "application/json" },
            consumes = { "multipart/form-data" },
            method = RequestMethod.POST)
    public ResponseEntity<List<User>> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        String message = "";
        try {

            message = storageService.store(file);

            List<User> users = storageService.openFile(message);

            userService.createUser(users);

            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }

    // build get user by id REST API
    // http://localhost:8080/api/users/1
    @GetMapping("{id}")
    public ResponseEntity<List<User>> getUserById(@PathVariable("id") Long userId){
        List<User> users = userService.findUserWithOrdersAndProducts(userId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // build search by userId and startDate and endDate REST API
    // http://localhost:8080/api/users/search/2/2021-07-14/2021-12-12
    @GetMapping("/search/{userId}/{startDate}/{endDate}")
    public ResponseEntity<List<User>> search(@PathVariable("userId") Long userId,
                                             @PathVariable("startDate") String startDate,
                                            @PathVariable("endDate") String endDate){
        List<User> users = userService.findUserWithOrdersAndProductsDate(userId, startDate, endDate);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Build Get All Users REST API
    // http://localhost:8080/api/users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
