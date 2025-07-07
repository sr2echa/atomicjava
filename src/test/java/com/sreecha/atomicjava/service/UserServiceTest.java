package com.sreecha.atomicjava.service;

import com.sreecha.atomicjava.dto.UserResponse;
import com.sreecha.atomicjava.model.Role;
import com.sreecha.atomicjava.model.User;
import com.sreecha.atomicjava.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .passwordHash("encodedPassword")
                .registrationDate(LocalDateTime.now())
                .roles(new HashSet<>(Arrays.asList(Role.USER)))
                .build();
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("new@example.com");
        newUser.setPasswordHash("rawPassword");
        newUser.setRoles(new HashSet<>(Arrays.asList(Role.USER)));

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        UserResponse response = userService.createUser(newUser);

        assertNotNull(response);
        assertEquals(newUser.getUsername(), response.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_UsernameExists() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        User newUser = new User();
        newUser.setUsername("testuser");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(newUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_EmailExists() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        User newUser = new User();
        newUser.setEmail("test@example.com");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(newUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals(user.getUsername(), response.getUsername());
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getAllUsers_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(Arrays.asList(user), pageable, 1);
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserResponse> responsePage = userService.getAllUsers(pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(user.getUsername(), responsePage.getContent().get(0).getUsername());
    }

    @Test
    void updateUser_Success() {
        User updatedDetails = new User();
        updatedDetails.setUsername("updateduser");
        updatedDetails.setEmail("updated@example.com");
        updatedDetails.setPasswordHash("newRawPassword");
        updatedDetails.setRoles(new HashSet<>(Arrays.asList(Role.ADMIN)));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.updateUser(1L, updatedDetails);

        assertNotNull(response);
        assertEquals(updatedDetails.getUsername(), response.getUsername());
        assertEquals(updatedDetails.getEmail(), response.getEmail());
        assertTrue(response.getRoles().contains(Role.ADMIN.name()));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(1L, new User()));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_NotFound() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void existsByUsername_True() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        assertTrue(userService.existsByUsername("testuser"));
    }

    @Test
    void existsByUsername_False() {
        when(userRepository.existsByUsername("nonexistent")).thenReturn(false);
        assertFalse(userService.existsByUsername("nonexistent"));
    }

    @Test
    void existsByEmail_True() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        assertTrue(userService.existsByEmail("test@example.com"));
    }

    @Test
    void existsByEmail_False() {
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);
        assertFalse(userService.existsByEmail("nonexistent@example.com"));
    }

    @Test
    void findByUsernameOrEmail_FoundByUsername() {
        when(userRepository.findByUsernameOrEmail("testuser")).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.findByUsernameOrEmail("testuser");
        assertTrue(foundUser.isPresent());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
    }

    @Test
    void findByUsernameOrEmail_FoundByEmail() {
        when(userRepository.findByUsernameOrEmail("test@example.com")).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.findByUsernameOrEmail("test@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void findByUsernameOrEmail_NotFound() {
        when(userRepository.findByUsernameOrEmail(anyString())).thenReturn(Optional.empty());
        Optional<User> foundUser = userService.findByUsernameOrEmail("nonexistent");
        assertFalse(foundUser.isPresent());
    }
}
