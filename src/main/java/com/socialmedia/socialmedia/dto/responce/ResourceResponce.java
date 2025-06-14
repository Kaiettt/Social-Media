package com.socialmedia.socialmedia.dto.responce;

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
public class ResourceResponce {
    private long id;
    private String resourceUrl;
    private String resourceType;
    private long postId;
}
