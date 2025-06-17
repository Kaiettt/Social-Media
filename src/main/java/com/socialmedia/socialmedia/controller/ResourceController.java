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
        if (multipartFiles == null || multipartFiles.length == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }

        // Validate file types and sizes if necessary
        for (MultipartFile file : multipartFiles) {
            if (file.isEmpty() || file.getSize() > 5 * 1024 * 1024) { // Example: max size 5MB
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
            }
        }

        // Call the service to handle the file upload
       return ResponseEntity.status(HttpStatus.CREATED).body(this.resourceService.createNewResource(multipartFiles));
    }

}
