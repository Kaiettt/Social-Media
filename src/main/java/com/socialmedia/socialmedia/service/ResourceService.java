package com.socialmedia.socialmedia.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.socialmedia.socialmedia.domain.Post;
import com.socialmedia.socialmedia.domain.Resource;
import com.socialmedia.socialmedia.dto.request.ResourceRequest;
import com.socialmedia.socialmedia.dto.responce.ResourceResponce;
import com.socialmedia.socialmedia.repository.ResourceRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ResourceService {
     private final ResourceRepository resourceRepository;
    private final StorageService storageService;

    public List<ResourceResponce> createNewResource(MultipartFile[] multipartFiles) {
        List<ResourceResponce> responses = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            try {
                
                String contentType = file.getContentType();
                boolean isVideo = contentType != null && contentType.startsWith("video/");

                String imageUrl = isVideo
                    ? this.storageService.uploadVideo(file, "videos").get("url").toString()
                    : this.storageService.uploadFile(file, "images").get("url").toString();


                ResourceResponce response = new ResourceResponce();
                response.setResourceUrl(imageUrl);
                response.setResourceType(file.getContentType());
                responses.add(response);

            } catch (Exception e) {
                System.out.println("Failed to upload: " + file.getOriginalFilename());
                e.printStackTrace();
            }
        }

        return responses;
    }


    public List<Resource> createNewResource(List<ResourceRequest> resources, Post post) {
        if (resources == null || resources.isEmpty()) {
            return new ArrayList<>();
        }

        List<Resource> resourceResponses = new ArrayList<>();
        for (ResourceRequest resourceRequest : resources) {
            Resource resource = Resource.builder()
                    .type(resourceRequest.getResourceType())
                    .url(resourceRequest.getResourceUrl())
                    .post(post)
                    .build();
            resource = this.resourceRepository.save(resource);
            resourceResponses.add(resource);
        }
        return resourceResponses;
    
    }

}
