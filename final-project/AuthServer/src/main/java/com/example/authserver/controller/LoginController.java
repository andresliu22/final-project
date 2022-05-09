package com.example.authserver.controller;

import com.example.authserver.constant.JwtConstant;
import com.example.authserver.domain.UserDomain;
import com.example.authserver.entity.User;
import com.example.authserver.repository.UserRepository;
import com.example.authserver.security.CookieUtil;
import com.example.authserver.security.JwtUtil;
import com.example.authserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User userLogged, HttpServletResponse res) {

        UserDomain user = userService.checkLogin(userLogged.getUserName(),userLogged.getUserPassword());

        String jwt = JwtUtil.generateToken(userLogged.getUserName(), JwtConstant.JWT_VALID_DURATION, user.getUserRole() ,user.getUserId());
        //Setting maxAge to -1 will preserve it until the browser is closed.
        CookieUtil.create(res, JwtConstant.JWT_COOKIE_NAME, jwt, false, -1, "localhost");

        return ResponseEntity.ok().body(jwt);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {

        userRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> findUser(@PathVariable Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok().body(user);
        } else {
            return null;
        }
    }

//    @GetMapping("/jwtInfo")
//    public ResponseEntity<String> getJwtInfo(@RequestParam String jwt) {
//        httpHeader jwt
//    }
}
