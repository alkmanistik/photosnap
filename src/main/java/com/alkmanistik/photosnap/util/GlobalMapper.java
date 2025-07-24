package com.alkmanistik.photosnap.util;

import com.alkmanistik.photosnap.dto.response.CommentResponse;
import com.alkmanistik.photosnap.dto.response.PostResponse;
import com.alkmanistik.photosnap.dto.response.PublicUserResponse;
import com.alkmanistik.photosnap.dto.response.UserResponse;
import com.alkmanistik.photosnap.model.Comment;
import com.alkmanistik.photosnap.model.Post;
import com.alkmanistik.photosnap.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public PostResponse mapToPostResponse(Post post,  UUID currentUserId) {
        return PostResponse.builder()
                .id(post.getId())
                .description(post.getDescription())
                .createdAt(post.getCreatedAt())
                .author(mapToUserResponse(post.getAuthor()))
                .likesCount(post.getLikes() != null ? post.getLikes().size() : 0)
                .comments(post.getComments() != null ?
                        post.getComments().stream()
                                .map(this::mapToCommentResponse)
                                .collect(Collectors.toList())
                        : Collections.emptyList())
                .likedByMe(post.getLikes() != null && currentUserId != null ?
                        post.getLikes().stream()
                                .anyMatch(like -> like.getUser().getId().equals(currentUserId))
                        : false)
                .isEdited(post.isEdited())
                .build();
    }

    public Page<PostResponse> mapToPostResponsePage(Page<Post> posts, UUID currentUserId) {
        return posts.map(post -> mapToPostResponse(post, currentUserId));
    }

    public CommentResponse mapToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .author(mapToUserResponse(comment.getAuthor()))
                .isEdited(comment.isEdited())
                .build();
    }
}
