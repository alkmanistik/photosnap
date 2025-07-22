package com.alkmanistik.photosnap.dto.request;

import com.alkmanistik.photosnap.model.User;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Data
public class ProfileUpdateRequest {
    @Size(max = 100)
    private String fullName;

    @Size(max = 500)
    private String bio;

    @Size(max = 20)
    private String phone;

    @Size(max = 255)
    @URL
    private String websiteUrl;

    @Size(max = 255)
    private String country;

    private User.Gender gender;
    private LocalDate birthDate;
}