package com.algonquin.profile.controllers;
import com.algonquin.profile.model.Credentials;
import com.algonquin.profile.model.User;
import com.algonquin.profile.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class ProfileController {
    @Autowired
    UserService userService;

    public ProfileController(){

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

}
