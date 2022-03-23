package com.algonquin.user.controllers;

import com.algonquin.user.model.Credentials;
import com.algonquin.user.model.User;
import com.algonquin.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    public UserController(){

    }
    @PostMapping("/recipe-book/registration")
    public User createUser(@RequestBody User s) throws SQLException {
        userService.register(s);
        return s;
    }
    @GetMapping(value = "/recipe-book/registration/{token}")
    public int validateUser(@PathVariable String token){
        return userService.validateUser(token);
    }
    @PostMapping("/recipe-book/login")
    public User login(@RequestBody Credentials cs) throws SQLException {

        User user = userService.login(cs);

        return user;
    }
    @PostMapping("/recipe-book/password-reset")
    public int changePassword(@RequestBody Credentials cs) {
        return userService.changePassword(cs);
    }
}
