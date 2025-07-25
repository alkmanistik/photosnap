package com.alkmanistik.photosnap.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Embedded
    @Builder.Default
    private Profile profile = new Profile();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isPrivate = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"})
    )
    @Column(name = "role", length = 20)
    @Builder.Default
    private Set<String> roles = new HashSet<>(Set.of("USER"));

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "target_id")
    )
    private Set<User> subscriptions = new HashSet<>();

    @ManyToMany(mappedBy = "subscriptions")
    private Set<User> subscribers = new HashSet<>();

    @Data
    @Embeddable
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Profile {
        @Column(length = 100)
        private String fullName;

        @Column(length = 20)
        private String phone;

        @Column(length = 500)
        private String bio;

        @Column(name = "avatar_name", length = 255)
        private String avatarName;

        @Column(name = "website_url", length = 255)
        private String websiteUrl;

        @Enumerated(EnumType.STRING)
        @Column(length = 10)
        private Gender gender;

        @Column(name = "birth_date")
        private LocalDate birthDate;

        @Column(length = 100)
        private String country;
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}