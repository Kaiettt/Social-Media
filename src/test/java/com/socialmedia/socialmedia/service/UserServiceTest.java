package com.socialmedia.socialmedia.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.socialmedia.socialmedia.common.Role;
import com.socialmedia.socialmedia.domain.User;
import com.socialmedia.socialmedia.dto.request.UserCreateRequest;
import com.socialmedia.socialmedia.dto.responce.UserCreateResponce;
import com.socialmedia.socialmedia.dto.responce.UserResponce;
import com.socialmedia.socialmedia.exception.EmailAlreadyExistsException;
import com.socialmedia.socialmedia.exception.EntityNotExistException;
import com.socialmedia.socialmedia.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        createRequest = new UserCreateRequest();
        createRequest.setFirstName("John");
        createRequest.setLastName("Doe");
        createRequest.setEmail("john.doe@example.com");
        createRequest.setPassword("password123");
        createRequest.setRole(Role.USER);
    }

    @Test
    void createNewUser_Success() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserCreateResponce response = userService.createNewUser(createRequest);

        // Assert
        assertNotNull(response);
        assertEquals(testUser.getId(), response.getId());
        assertEquals(testUser.getFirstName(), response.getFirstName());
        assertEquals(testUser.getLastName(), response.getLastName());
        assertEquals(testUser.getEmail(), response.getUserName());
        assertEquals(testUser.getRole(), response.getRole());

        verify(userRepository).existsByEmail(createRequest.getEmail());
        verify(passwordEncoder).encode(createRequest.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createNewUser_EmailExists_ThrowsException() {
        // Arrange
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> userService.createNewUser(createRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getAllUsers_Success() {
        // Arrange
        List<User> users = Arrays.asList(testUser);
        when(userRepository.count()).thenReturn(1L);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponce> responses = userService.getAllUsers();

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(testUser.getId(), responses.get(0).getId());
        assertEquals(testUser.getFirstName(), responses.get(0).getFirstName());
        assertEquals(testUser.getLastName(), responses.get(0).getLastName());
        assertEquals(testUser.getEmail(), responses.get(0).getUserName());
        assertEquals(testUser.getRole(), responses.get(0).getRole());

        verify(userRepository).count();
        verify(userRepository).findAll();
    }

    @Test
    void getAllUsers_EmptyList_ThrowsException() {
        // Arrange
        when(userRepository.count()).thenReturn(0L);

        // Act & Assert
        assertThrows(EntityNotExistException.class, () -> userService.getAllUsers());
        verify(userRepository, never()).findAll();
    }

    @Test
    void getUserByUserName_Success() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.getUserByUserName(testUser.getEmail());

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getFirstName(), result.getFirstName());
        assertEquals(testUser.getLastName(), result.getLastName());
        assertEquals(testUser.getRole(), result.getRole());

        verify(userRepository).findByEmail(testUser.getEmail());
    }

    @Test
    void getUserByUserName_NotFound_ThrowsException() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotExistException.class, () -> userService.getUserByUserName("nonexistent@example.com"));
    }

    @Test
    void updateUser_Success() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.updateUser(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getFirstName(), result.getFirstName());
        assertEquals(testUser.getLastName(), result.getLastName());
        assertEquals(testUser.getRole(), result.getRole());

        verify(userRepository).save(testUser);
    }

    @Test
    void getUserByRefreshToken_Success() {
        // Arrange
        String refreshToken = "testRefreshToken";
        testUser.setRefreshToken(refreshToken);
        when(userRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.getUserByRefreshToken(refreshToken);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(refreshToken, result.getRefreshToken());
        assertEquals(testUser.getEmail(), result.getEmail());

        verify(userRepository).findByRefreshToken(refreshToken);
    }

    @Test
    void getUserByRefreshToken_NotFound_ThrowsException() {
        // Arrange
        when(userRepository.findByRefreshToken(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotExistException.class, () -> userService.getUserByRefreshToken("invalidToken"));
    }
} 