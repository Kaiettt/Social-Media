package com.socialmedia.socialmedia.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.socialmedia.socialmedia.domain.Post;
import com.socialmedia.socialmedia.domain.Resource;
import com.socialmedia.socialmedia.dto.request.PostCreateRequest;
import com.socialmedia.socialmedia.dto.responce.PostResponce;
import com.socialmedia.socialmedia.dto.responce.ResourceResponce;
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

        List<ResourceResponce> resourceResponses = new ArrayList<>();
            for (Resource resource : newPost.getResources()) {
                ResourceResponce resourceResponse = ResourceResponce.builder()
                    .resourceUrl(resource.getUrl())
                    .resourceType(resource.getType())
                    .build();
                resourceResponses.add(resourceResponse);
            }

        return PostResponce.builder()
                .id(newPost.getId())    
                .content(newPost.getContent())
                .resources(resourceResponses)
                .userId(request.getUserId())
                .build();
    }

    public List<PostResponce> getUserNewsFeed() {
        List<Post> posts = this.postRepository.findAll();
        List<PostResponce> postResponses = new ArrayList<>();

        for (Post post : posts) {
            List<ResourceResponce> resourceResponses = new ArrayList<>();
            for (Resource resource : post.getResources()) {
                ResourceResponce resourceResponse = ResourceResponce.builder()
                    .resourceUrl(resource.getUrl())
                    .resourceType(resource.getType())
                    .build();
                resourceResponses.add(resourceResponse);
            }

            PostResponce postResponse = PostResponce.builder()
                .id(post.getId())
                .content(post.getContent())
                .userName(post.getUser().getLastName() + " " + post.getUser().getFirstName())
                .resources(resourceResponses)
                .userId(post.getUser().getId())
                .build();

            postResponses.add(postResponse);
        }

        return postResponses;
    }
} 
    

