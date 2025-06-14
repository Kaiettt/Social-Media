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

        for (MultipartFile multipartFile : multipartFiles) {
            try {
                File file = convertMultipartToFile(multipartFile);

                String imageUrl = storageService.uploadImageToDrive(file);

                ResourceResponce response = new ResourceResponce();
                response.setResourceUrl(imageUrl);
                response.setResourceType(multipartFile.getContentType());
                responses.add(response);

            } catch (Exception e) {
                System.out.println("Failed to upload: " + multipartFile.getOriginalFilename());
                e.printStackTrace();
            }
        }

        return responses;
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws Exception {
        File convFile = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipartFile.getBytes());
        }
        return convFile;
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
