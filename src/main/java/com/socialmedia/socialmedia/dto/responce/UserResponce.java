package com.socialmedia.socialmedia.dto.responce;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.socialmedia.socialmedia.common.Role;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponce {
    private long id;
    private String firstName;
    private String lastName;
    private String userName;
    private Role role;
}
