package com.alkmanistik.photosnap.dto.response;

import com.alkmanistik.photosnap.model.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PublicUserResponse {
    private UUID id;
    private String username;
    private User.Profile profile;
    private LocalDateTime createdAt;
}