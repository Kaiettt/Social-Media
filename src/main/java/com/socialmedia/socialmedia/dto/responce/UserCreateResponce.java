package com.socialmedia.socialmedia.dto.responce;

import com.socialmedia.socialmedia.common.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateResponce {
    private long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String profilePictureUrl;
    private String bio;
    private String phoneNumber;
    private int followersCount;
    private int followingCount;
    private Role role;
}
