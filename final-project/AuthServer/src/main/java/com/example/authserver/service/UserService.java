package com.example.authserver.service;

import com.example.authserver.domain.UserDomain;
import com.example.authserver.entity.User;
import com.example.authserver.exception.NoUserFoundException;
import com.example.authserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public UserDomain checkLogin(String username, String password) {
        Optional<User> userOptional = userRepository.findUserByUserName(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            UserDomain userDomain = UserDomain
                    .builder()
                    .userId(user.getUserId())
                    .userName(user.getUsername())
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

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findUserByUserName(s).orElseThrow(() -> new UsernameNotFoundException("Username: " + s + " not found"));
    }

}


