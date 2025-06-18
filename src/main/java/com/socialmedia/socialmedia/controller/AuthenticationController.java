package com.socialmedia.socialmedia.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.socialmedia.socialmedia.common.Common;
import com.socialmedia.socialmedia.config.ApiMessage;
import com.socialmedia.socialmedia.dto.request.LoginDTO;
import com.socialmedia.socialmedia.dto.request.SignupRequest;
import com.socialmedia.socialmedia.dto.request.VerficationRequest;
import com.socialmedia.socialmedia.dto.responce.LoginResponce;
import com.socialmedia.socialmedia.dto.responce.SignupResponce;
import com.socialmedia.socialmedia.exception.VerificationException;
import com.socialmedia.socialmedia.service.AuthenicationService;
import com.socialmedia.socialmedia.service.UserService;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
   private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthenicationService authenicationService;
    private final  UserService userService;
    public AuthenticationController( UserService userService,AuthenicationService authenicationService,AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.authenicationService = authenicationService;
      this.userService = userService;
    }
    
    @PostMapping("/login")
    @ApiMessage("Login succesfully")
    public ResponseEntity<LoginResponce> login(@RequestBody LoginDTO loginDto)  {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginResponce loginResponce = this.authenicationService.handleLoginResponce(authentication,loginDto.getUsername());
        
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,loginResponce.getSpringCookie().toString()).body(loginResponce);
    }

    @PostMapping("/signup")
    @ApiMessage("Signup succesfully")
    public ResponseEntity<SignupResponce> login(@Valid @RequestBody SignupRequest request)  {

        SignupResponce user = this.authenicationService.handleSignupUser(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    @PutMapping("/confirm")
    public ResponseEntity<SignupResponce> confirm(@Valid @RequestBody VerficationRequest request) throws VerificationException {
        return ResponseEntity.ok().body(this.authenicationService.handleConfirmation(request.getEmail(),Long.parseLong(request.getToken())));
    }

    @PutMapping("/resend-code")
    public ResponseEntity<Void> resendCode(@RequestParam("email") String email) throws EntityExistsException {
        this.authenicationService.handleResendToken(email);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/refresh")
    @ApiMessage("Get Access Token")
    public ResponseEntity<LoginResponce> getAccessToken(@CookieValue(name = "refresh-token", defaultValue = "") String refresh_token) throws EntityExistsException {
        if(refresh_token == null || refresh_token.isEmpty()) {
            throw new EntityExistsException(Common.REFRESH_TOKEN_NOT_FOUND);
        }
        LoginResponce loginResponce = this.authenicationService.getAccessToken(refresh_token);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, loginResponce.getSpringCookie().toString())
                .body(loginResponce);
    }
}
