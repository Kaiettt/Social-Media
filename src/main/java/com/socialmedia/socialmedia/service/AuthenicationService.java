package com.socialmedia.socialmedia.service;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.socialmedia.socialmedia.domain.User;
import com.socialmedia.socialmedia.dto.responce.LoginResponce;
import com.socialmedia.socialmedia.util.SecurityUtil;

import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthenicationService {
      private final UserService userService;
    private final SecurityUtil securityUtil;
    public LoginResponce handleLoginResponce(Authentication authentication, String username) {
        User user = this.userService.getUserByUserName(username);
        LoginResponce.UserLogin userLogin = new LoginResponce.UserLogin();
        userLogin.setFirstName(user.getFirstName());
        userLogin.setLastName(user.getLastName());
        userLogin.setId(user.getId());
        userLogin.setRole(user.getRole());
        userLogin.setUserName(username);
        String accessToken = this.securityUtil.createToken(username,user);
        String refreshToken = this.securityUtil.createRefreshToken(username, user);
        user.setRefreshToken(refreshToken);
        this.userService.updateUser(user);

        ResponseCookie springCookie = ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(this.securityUtil.jwtRefreshTokenExpiration)
                .build();
        LoginResponce loginResponce = LoginResponce.builder()
            .accessToken(accessToken)
            .springCookie(springCookie)
            .user(userLogin)
            .build();
        return loginResponce;
    }
    public LoginResponce getAccessToken(String refresh_token) throws EntityExistsException{
        User user = this.userService.getUserByRefreshToken(refresh_token);
        LoginResponce.UserLogin userResponce = new LoginResponce.UserLogin();
        userResponce.setId((user.getId()));
        userResponce.setUserName(user.getEmail());
        userResponce.setFirstName(user.getFirstName());
        userResponce.setLastName(user.getLastName());
        String accessToken = this.securityUtil.createToken(user.getEmail(),user);
        LoginResponce loginResponce = new LoginResponce();
        loginResponce.setUser(userResponce);
        loginResponce.setAccessToken(accessToken);

        String refreshToken = this.securityUtil.createRefreshToken(user.getEmail(), user);
        user.setRefreshToken(refreshToken);
        this.userService.updateUser(user);


        ResponseCookie springCookie = ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(this.securityUtil.jwtRefreshTokenExpiration)
                .build();
        loginResponce.setSpringCookie(springCookie);
        return loginResponce;
    }
}
