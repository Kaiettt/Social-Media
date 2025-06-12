package com.socialmedia.socialmedia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.socialmedia.socialmedia.domain.User;
import com.socialmedia.socialmedia.dto.request.UserCreateRequest;
import com.socialmedia.socialmedia.dto.responce.UserCreateResponce;
import com.socialmedia.socialmedia.dto.responce.UserResponce;
import com.socialmedia.socialmedia.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserCreateResponce> createNewUser(@RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createNewUser(request));
    }
    @GetMapping("/users")
    public ResponseEntity<List<UserResponce>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsers());
    }
    
}
