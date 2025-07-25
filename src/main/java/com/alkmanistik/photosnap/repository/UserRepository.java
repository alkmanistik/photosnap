package com.alkmanistik.photosnap.repository;

import com.alkmanistik.photosnap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>{

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByIsPrivateFalse();

    @Query("SELECT u FROM User u WHERE u.isPrivate = false AND (LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR (LOWER(u.profile.fullName) LIKE LOWER(CONCAT('%', :query, '%'))))")
    List<User> searchPublicUsers(String query);

    @Query("SELECT u.id FROM User u JOIN u.subscribers s WHERE s.id = :userId")
    Set<UUID> findSubscriptionIdsByUserId(@Param("userId") UUID userId);

    @Query("SELECT u FROM User u JOIN u.subscribers s WHERE s.id = :userId")
    List<User> findSubscriptionsByUserId(UUID userId);

}