package com.socialmedia.socialmedia.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.socialmedia.socialmedia.domain.Post;
import com.socialmedia.socialmedia.domain.Resource;
import com.socialmedia.socialmedia.dto.request.PostCreateRequest;
import com.socialmedia.socialmedia.dto.responce.PostResponce;
import com.socialmedia.socialmedia.repository.PostRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PostService {
    private final UserService userService;
    private final ResourceService resourceService;
    private final PostRepository postRepository;
    public PostResponce createNewPost(PostCreateRequest request) {
         Post newPost = Post.builder()
                .user(this.userService.getUserById(request.getUserId()))
                .content(request.getContent())
                .build();

        newPost = this.postRepository.save(newPost);

        List<Resource> resources = this.resourceService.createNewResource(request.getResources(), newPost);
        newPost.setResources(resources);
        newPost = this.postRepository.save(newPost);

        return PostResponce.builder()
                .id(newPost.getId())
                .content(newPost.getContent())
                .resources(
                    resources.stream()
                             .map(Resource::getUrl)
                             .collect(Collectors.toList())
                )
                .userId(request.getUserId())
                .build();
    }
    } 
    

