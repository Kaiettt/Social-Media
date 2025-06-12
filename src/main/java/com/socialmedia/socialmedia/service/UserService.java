package com.socialmedia.socialmedia.service;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.socialmedia.socialmedia.common.Common;
import com.socialmedia.socialmedia.domain.User;
import com.socialmedia.socialmedia.dto.request.UserCreateRequest;
import com.socialmedia.socialmedia.dto.responce.UserCreateResponce;
import com.socialmedia.socialmedia.dto.responce.UserResponce;
import com.socialmedia.socialmedia.exception.EntityNotExistException;
import com.socialmedia.socialmedia.repository.UserRepository;

import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class UserService {
     private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserCreateResponce createNewUser(UserCreateRequest request){
        User user = User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(this.passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .build();
        user  = this.userRepository.save(user);
        UserCreateResponce responce = UserCreateResponce.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .userName(user.getEmail())
            .role(user.getRole())
            .build();
        return responce;
    }
    public List<UserResponce> getAllUsers() {
        if (this.userRepository.count() == 0) {
            throw new EntityNotExistException(Common.USER_NOT_FOUND);
        }
        List<User> users = this.userRepository.findAll();
        List<UserResponce> userResponces = users.stream()
            .map(user -> UserResponce.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getEmail())
                .role(user.getRole())
                .build())
            .toList();
        return userResponces;
    }
    public User getUserByUserName(String username) {
        return this.userRepository.findByEmail(username)
            .orElseThrow(() -> new EntityNotExistException(Common.USER_NOT_FOUND));
    }
    public User updateUser(User user) {
        return this.userRepository.save(user);
    }
    public User getUserByRefreshToken(String refresh_token) {
        return this.userRepository.findByRefreshToken(refresh_token)
        .orElseThrow(() -> new EntityNotExistException(Common.REFRESH_TOKEN_NOT_FOUND));
    }
}
