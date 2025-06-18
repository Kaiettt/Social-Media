package com.socialmedia.socialmedia.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.socialmedia.socialmedia.common.Common;
import com.socialmedia.socialmedia.common.Role;
import com.socialmedia.socialmedia.domain.ConfirmationToken;
import com.socialmedia.socialmedia.domain.User;
import com.socialmedia.socialmedia.dto.request.SignupRequest;
import com.socialmedia.socialmedia.dto.responce.LoginResponce;
import com.socialmedia.socialmedia.dto.responce.SignupResponce;
import com.socialmedia.socialmedia.exception.EmailAlreadyExistsException;
import com.socialmedia.socialmedia.exception.VerificationException;
import com.socialmedia.socialmedia.repository.ConfirmationTokenRepository;
import com.socialmedia.socialmedia.repository.UserRepository;
import com.socialmedia.socialmedia.util.SecurityUtil;

import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthenicationService {
      private final UserService userService;
    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private PasswordEncoder passwordEncoder;
    private final MailService mailService;
    public LoginResponce handleLoginResponce(Authentication authentication, String username) {
        User user = this.userService.getUserByUserName(username);
        LoginResponce.UserLogin userLogin = new LoginResponce.UserLogin();
        userLogin.setFirstName(user.getFirstName());
        userLogin.setLastName(user.getLastName());
        userLogin.setId(user.getId());
        userLogin.setRole(user.getRole());
        userLogin.setUserName(username);
        userLogin.setAvatar(user.getAvartar());
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

    public SignupResponce handleSignupUser(SignupRequest request) {
        if (this.userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(Common.USER_ALREADY_EXIST);
        }
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .profilePictureUrl(request.getProfilePictureUrl() == null ? "" : request.getProfilePictureUrl())
                .bio(request.getBio() == null ? "" : request.getBio())
                .followersCount(0)
                .followingCount(0)
                .isVerified(false)
                .phoneNumber(request.getPhoneNumber() == null ? "" : request.getPhoneNumber())
                .role(Role.USER)
                .build();
        user = this.userRepository.save(user);
        long token = createToken(user);
        this.mailService.send(request.getEmail(), user.getFirstName() + user.getLastName(),token);
        SignupResponce responce = SignupResponce.builder()
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .build();
        return responce;
    }
    private long createToken(User user) {
        Random random = new Random();
        long token = 100000 + random.nextInt(900000);
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
            .token(token)
            .user(user)
            .expiresAt(LocalDateTime.now().plusMinutes(5))
            .build();
        this.confirmationTokenRepository.save(confirmationToken);
        return token;
    }
    public SignupResponce handleConfirmation(String email, long token) throws VerificationException {
        ConfirmationToken confirmationToken = this.confirmationTokenRepository.findByTokenAndUserEmail(token,email).orElseThrow(
            () -> new VerificationException("token is not found.")
        );
       if(confirmationToken.getConfirmedAt() != null){
            throw new VerificationException("email is already confirmed");
        }
        if(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new VerificationException("email is already expired");
        }
        this.confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now(), email);
        User user = this.userService.getUserById(confirmationToken.getUser().getId());
        user.setIsVerified(true);
        this.userRepository.save(user);

        SignupResponce responce = SignupResponce.builder()
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .build();

        return responce;
    }

    public void handleResendToken(String email) throws EntityExistsException {
        if (!this.userRepository.existsByEmail(email)) {
            throw new EntityExistsException(Common.USER_NOT_FOUND);
        }
        User user = this.userService.getUserByUserName(email);
        long token = createToken(user);
        this.mailService.send(email, user.getFirstName() + user.getLastName(), token);
    }
}
