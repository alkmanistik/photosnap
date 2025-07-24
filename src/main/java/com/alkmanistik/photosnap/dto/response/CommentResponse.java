package com.alkmanistik.photosnap.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CommentResponse {
    private UUID id;
    private String text;
    private LocalDateTime createdAt;
    private UserResponse author;
    private boolean isEdited;
}
