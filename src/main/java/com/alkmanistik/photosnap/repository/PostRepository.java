package com.alkmanistik.photosnap.repository;

import com.alkmanistik.photosnap.model.Post;
import com.alkmanistik.photosnap.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    @EntityGraph(attributePaths = {"author", "comments.author", "likes.user"})
    Page<Post> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);
    @EntityGraph(attributePaths = {"author", "comments.author", "likes.user"})
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    @EntityGraph(attributePaths = {"author"})
    Page<Post> findByAuthorIsPrivateFalseOrderByCreatedAtDesc(Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.author.id IN :authorIds AND " +
            "(p.author.isPrivate = false OR p.author.id IN :authorIds) " +
            "ORDER BY p.createdAt DESC")
    Page<Post> findByAuthorIdInAndPrivacy(
            @Param("authorIds") Set<UUID> authorIds,
            @Param("isPrivate") boolean isPrivate,
            Pageable pageable);
}