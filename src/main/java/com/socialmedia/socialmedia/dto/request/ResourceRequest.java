package com.socialmedia.socialmedia.dto.request;

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
public class ResourceRequest {
    @NotBlank(message = "resource URL cannot be blank")
    private String resourceUrl;
    private String resourceType;
}
