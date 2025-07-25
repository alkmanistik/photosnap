package com.alkmanistik.photosnap.service;

import com.alkmanistik.photosnap.dto.request.ChangePasswordRequest;
import com.alkmanistik.photosnap.dto.request.ProfileUpdateRequest;
import com.alkmanistik.photosnap.exception.ForbiddenException;
import com.alkmanistik.photosnap.exception.UserNotFound;
import com.alkmanistik.photosnap.model.User;
import com.alkmanistik.photosnap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    public User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("User not found"));
    }

    public User updateProfile(Authentication authentication, ProfileUpdateRequest request, MultipartFile avatarFile) {
        User currentUser = getCurrentUser(authentication);
        User.Profile profile = User.Profile.builder()
                .fullName(request.getFullName())
                .bio(request.getBio())
                .phone(request.getPhone())
                .birthDate(request.getBirthDate())
                .websiteUrl(request.getWebsiteUrl())
                .country(request.getCountry())
                .gender(request.getGender())
                .build();

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarName = storageService.store(avatarFile, "avatars");
            profile.setAvatarName(avatarName);
        }

        currentUser.setProfile(profile);
        return userRepository.save(currentUser);
    }

    public User togglePrivacy(Authentication authentication, boolean isPrivate) {
        User currentUser = getCurrentUser(authentication);
        currentUser.setIsPrivate(isPrivate);
        return userRepository.save(currentUser);
    }

    public User getUser(Authentication authentication, UUID userId) {
        User currentUser = getCurrentUser(authentication);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound("User not found"));

        if (!currentUser.getRoles().contains("ADMIN")) {
            if (user.getIsPrivate()) {
                throw new ForbiddenException("This user's profile is private");
            }
        }

        return user;
    }

    public void changePassword(Authentication authentication, ChangePasswordRequest request) {

        User user = getCurrentUser(authentication);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new ForbiddenException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

    }

    public List<User> getPublicUser() {
        return userRepository.findByIsPrivateFalse();
    }

    public List<User> searchPublicUsers(String query) {
        return userRepository.searchPublicUsers(query);
    }

    public void subscribe(UUID subscriberId, UUID targetId) {
        User subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> new UserNotFound("Subscriber not found"));
        User target = userRepository.findById(targetId)
                .orElseThrow(() -> new UserNotFound("Target user not found"));

        subscriber.getSubscriptions().add(target);
        userRepository.save(subscriber);
    }

    public void unsubscribe(UUID subscriberId, UUID targetId) {
        User subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> new UserNotFound("Subscriber not found"));
        User target = userRepository.findById(targetId)
                .orElseThrow(() -> new UserNotFound("Target user not found"));

        subscriber.getSubscriptions().remove(target);
        userRepository.save(subscriber);
    }

    public List<User> getUserSubscriptions(UUID userId) {
        return userRepository.findSubscriptionsByUserId(userId);
    }

}
