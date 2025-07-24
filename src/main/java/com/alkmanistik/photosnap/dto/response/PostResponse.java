package com.alkmanistik.photosnap.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PostResponse {
    private UUID id;
    private String description;
    private String imageName;
    private LocalDateTime createdAt;
    private UserResponse author;
    private int likesCount;
    private List<CommentResponse> comments;
    private boolean likedByMe;
    private boolean isEdited;
}