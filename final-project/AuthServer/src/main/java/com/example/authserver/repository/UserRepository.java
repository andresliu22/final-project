package com.example.authserver.repository;

import com.example.authserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
        Optional<User> findUserByUserName(String userName);
}
