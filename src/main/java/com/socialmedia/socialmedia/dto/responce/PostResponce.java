package com.socialmedia.socialmedia.dto.responce;

import java.util.List;


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
public class PostResponce {
    private long id;
    private long userId;
    private String content;
    private List<String> resources;
    private String createdAt;
    private String updatedAt;
}
