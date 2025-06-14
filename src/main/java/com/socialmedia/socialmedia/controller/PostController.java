package com.socialmedia.socialmedia.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.socialmedia.socialmedia.dto.request.PostCreateRequest;
import com.socialmedia.socialmedia.dto.responce.PostResponce;
import com.socialmedia.socialmedia.service.PostService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostResponce> createNewUser(@RequestBody PostCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.postService.createNewPost(request));
    }
}
