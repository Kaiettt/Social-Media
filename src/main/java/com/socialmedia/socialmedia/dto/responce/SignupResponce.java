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
public class SignupResponce {
    private String firstName;
    private String lastName; 
    private String email;
}
