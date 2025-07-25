package com.alkmanistik.photosnap.contoller;

import com.alkmanistik.photosnap.dto.response.PostResponse;
import com.alkmanistik.photosnap.model.User;
import com.alkmanistik.photosnap.service.PostService;
import com.alkmanistik.photosnap.service.UserService;
import com.alkmanistik.photosnap.util.GlobalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final UserService userService;
    private final PostService postService;
    private final GlobalMapper mapper;

    @PostMapping("/{targetId}/subscribe")
    public void subscribe(@PathVariable UUID targetId, Authentication authentication) {
        UUID subscriberId = ((User) authentication.getPrincipal()).getId();
        userService.subscribe(subscriberId, targetId);
    }

    @PostMapping("/{targetId}/unsubscribe")
    public void unsubscribe(@PathVariable UUID targetId, Authentication authentication) {
        UUID subscriberId = ((User) authentication.getPrincipal()).getId();
        userService.unsubscribe(subscriberId, targetId);
    }

    @GetMapping("/posts")
    public Page<PostResponse> getSubscriptionPosts(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication) {
        UUID userId = ((User) authentication.getPrincipal()).getId();
        return postService.getPostsFromSubscriptions(userId, pageable)
                .map(post -> mapper.mapToPostResponse(post, userId));
    }
}