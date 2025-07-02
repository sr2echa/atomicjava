package com.sreecha.atomicjava.service;

import com.sreecha.atomicjava.dto.UserResponse;
import com.sreecha.atomicjava.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);
    UserResponse createUser(User user);
    UserResponse getUserById(Long id);
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse updateUser(Long id, User userDetails);
    void deleteUser(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}