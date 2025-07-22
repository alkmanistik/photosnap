package com.alkmanistik.photosnap.util;

import com.alkmanistik.photosnap.dto.response.PublicUserResponse;
import com.alkmanistik.photosnap.dto.response.UserResponse;
import com.alkmanistik.photosnap.model.User;
import org.springframework.stereotype.Component;

@Component
public class GlobalMapper {

    public UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profile(user.getProfile())
                .createdAt(user.getCreatedAt())
                .isPrivate(user.getIsPrivate())
                .build();
    }

    public PublicUserResponse mapToPublicUserResponse(User user) {
        return PublicUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .profile(user.getProfile())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
