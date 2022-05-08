package com.example.authserver.service;

import com.example.authserver.domain.UserDomain;
import com.example.authserver.entity.User;
import com.example.authserver.exception.NoUserFoundException;
import com.example.authserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDomain checkLogin(String username, String password) {
        Optional<User> userOptional = userRepository.findUserByUserName(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            UserDomain userDomain = UserDomain
                    .builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .userPassword(user.getUserPassword())
                    .userRole(user.getUserRole())
                    .build();

            if (userDomain.getUserPassword().equals(password)) {
                return userDomain;
            } else {
                throw new NoUserFoundException(String.format("User with username [%s] not found", username));
            }
        } else {
            throw new NoUserFoundException(String.format("User with username [%s] not found", username));
        }
    }

}


