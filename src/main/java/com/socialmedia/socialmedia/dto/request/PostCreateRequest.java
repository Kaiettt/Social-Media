package com.socialmedia.socialmedia.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostCreateRequest {
    @NotBlank(message = "User ID cannot be blank")
    private int userId;
    @NotBlank(message = "Content cannot be blank")
    private String content;
    List<ResourceRequest> resources;
}
