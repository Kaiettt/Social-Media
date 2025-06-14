package com.socialmedia.socialmedia.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.socialmedia.socialmedia.dto.responce.ResourceResponce;
import com.socialmedia.socialmedia.service.ResourceService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class ResourceController {
    private final ResourceService resourceService;
    @PostMapping("/upload")
    public ResponseEntity<List<ResourceResponce>> uploadResource(@RequestParam("files") MultipartFile[] multipartFiles) {
       return ResponseEntity.status(HttpStatus.CREATED).body(this.resourceService.createNewResource(multipartFiles));
    }

}
