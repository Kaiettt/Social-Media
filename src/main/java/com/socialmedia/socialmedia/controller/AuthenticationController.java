package com.socialmedia.socialmedia.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.socialmedia.socialmedia.common.Common;
import com.socialmedia.socialmedia.config.ApiMessage;
import com.socialmedia.socialmedia.dto.request.LoginDTO;
import com.socialmedia.socialmedia.dto.responce.LoginResponce;
import com.socialmedia.socialmedia.service.AuthenicationService;
import com.socialmedia.socialmedia.service.UserService;

import jakarta.persistence.EntityExistsException;

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
