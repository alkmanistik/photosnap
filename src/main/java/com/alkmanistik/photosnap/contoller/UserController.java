package com.alkmanistik.photosnap.contoller;

import com.alkmanistik.photosnap.dto.request.ChangePasswordRequest;
import com.alkmanistik.photosnap.dto.request.ProfileUpdateRequest;
import com.alkmanistik.photosnap.dto.response.PublicUserResponse;
import com.alkmanistik.photosnap.dto.response.UserResponse;
import com.alkmanistik.photosnap.model.User;
import com.alkmanistik.photosnap.service.UserService;
import com.alkmanistik.photosnap.util.GlobalMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final GlobalMapper globalMapper;

    @GetMapping("/me")
    public UserResponse getCurrentUser(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        return globalMapper.mapToUserResponse(user);
    }

    @PutMapping(value = "/me/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponse updateProfile(
            @Valid @RequestPart ProfileUpdateRequest request,
            @RequestPart(required = false) MultipartFile avatar,
            Authentication authentication) {
        User updatedUser = userService.updateProfile(authentication, request, avatar);
        return globalMapper.mapToUserResponse(updatedUser);
    }

    @GetMapping("/public")
    public List<PublicUserResponse> getPublicUsers() {
        List<User> publicUsers = userService.getPublicUser();
        return publicUsers.stream()
                .map(globalMapper::mapToPublicUserResponse)
                .collect(Collectors.toList());
    }

    @PatchMapping("/me/privacy")
    public UserResponse togglePrivacy(
            @RequestParam boolean isPrivate,
            Authentication authentication) {
        User user = userService.togglePrivacy(authentication, isPrivate);
        return globalMapper.mapToUserResponse(user);
    }

    @GetMapping("/{userId}")
    public UserResponse getUserById(
            @PathVariable UUID userId, Authentication authentication) {
        User user = userService.getUser(authentication, userId);
        return globalMapper.mapToUserResponse(user);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PublicUserResponse>> searchUsers(
            @RequestParam String query) {
        List<User> users = userService.searchPublicUsers(query);
        return ResponseEntity.ok(users.stream()
                .map(globalMapper::mapToPublicUserResponse)
                .collect(Collectors.toList()));
    }

    @PostMapping("/me/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        userService.changePassword(authentication, request);
    }

}
