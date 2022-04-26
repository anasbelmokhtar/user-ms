package org.ac.cst8277.belmokhtar.anas.controllers;

import org.ac.cst8277.belmokhtar.anas.model.Credentials;
import org.ac.cst8277.belmokhtar.anas.model.User;
import org.ac.cst8277.belmokhtar.anas.services.UserService;
import org.ac.cst8277.belmokhtar.anas.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    public UserController(){

    }
    @PostMapping("/twitter-like-app/registration")
    public User createUser(@RequestBody User s) throws SQLException {

        userService.register(s);
        return s;
    }
    @GetMapping(value = "/twitter-like-app/registration/{token}")
    public int validateUser(@PathVariable String token){
        return userService.validateUser(token);
    }

    @GetMapping(value = "/twitter-like-app/roles")
    public ResponseEntity<List<String>> getAllRoles() {
        return ResponseEntity.ok(userService.getAllRoles());
    }

    @GetMapping(value = "/twitter-like-app/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/twitter-like-app/login")
    public String login(@RequestBody Credentials cs) throws SQLException {
        String token = userService.login(cs);
        return token;
    }

    @GetMapping("/login/oauth2/code/github")
    public String login(@RequestParam (value="code") String code) {
        System.out.println(code);
        //String token = userService.loginOauth();
       // return token;
        return null;
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @PostMapping("/twitter-like-app/password-reset")
    public int changePassword(@RequestBody Credentials cs) {
        return userService.changePassword(cs);
    }

}
