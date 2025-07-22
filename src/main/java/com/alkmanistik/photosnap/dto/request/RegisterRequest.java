package com.alkmanistik.photosnap.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 63)
    private String username;
    @Email
    private String email;
    @NotBlank
    @Size(min = 6, max = 127)
    private String password;
}
