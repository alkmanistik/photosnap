package com.alkmanistik.photosnap.contoller;

import com.alkmanistik.photosnap.dto.response.CommentResponse;
import com.alkmanistik.photosnap.dto.response.PostResponse;
import com.alkmanistik.photosnap.model.Comment;
import com.alkmanistik.photosnap.model.Post;
import com.alkmanistik.photosnap.model.User;
import com.alkmanistik.photosnap.service.PostService;
import com.alkmanistik.photosnap.util.GlobalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final GlobalMapper mapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostResponse createPost(
            @RequestPart String description,
            @RequestPart MultipartFile image,
            Authentication authentication) {
        Post post = postService.createPost(authentication, description, image);
        return mapper.mapToPostResponse(post, null);
    }

    @GetMapping
    public Page<PostResponse> getAllPosts(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication) {

        UUID currentUserId = authentication != null ?
                ((User) authentication.getPrincipal()).getId() : null;


        return mapper.mapToPostResponsePage(
                postService.getAllPosts(pageable),
                currentUserId
        );
    }

    @GetMapping("/{postId}")
    public PostResponse getPostById(
            @PathVariable UUID postId,
            Authentication authentication) {

        UUID currentUserId = authentication != null ?
                ((User) authentication.getPrincipal()).getId() : null;

        return mapper.mapToPostResponse(
                postService.getPostById(postId),
                currentUserId
        );
    }

    @PostMapping("/{postId}/comments")
    public CommentResponse addComment(
            @PathVariable UUID postId,
            @RequestBody String text,
            Authentication authentication) {
        Comment comment = postService.addComment(authentication, postId, text);
        return mapper.mapToCommentResponse(comment);
    }

    @PostMapping("/{postId}/likes")
    public void toggleLike(
            @PathVariable UUID postId,
            Authentication authentication) {
        postService.toggleLike(authentication, postId);
    }
}