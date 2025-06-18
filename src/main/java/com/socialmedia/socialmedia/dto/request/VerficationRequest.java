package com.socialmedia.socialmedia.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VerficationRequest {
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Token is required")
    private String token;
}
