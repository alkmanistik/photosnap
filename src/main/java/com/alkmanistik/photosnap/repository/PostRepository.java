package com.alkmanistik.photosnap.repository;

import com.alkmanistik.photosnap.model.Post;
import com.alkmanistik.photosnap.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    @EntityGraph(attributePaths = {"author", "comments.author", "likes.user"})
    Page<Post> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);
    @EntityGraph(attributePaths = {"author", "comments.author", "likes.user"})
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    @EntityGraph(attributePaths = {"author"})
    Page<Post> findByAuthorIsPrivateFalseOrderByCreatedAtDesc(Pageable pageable);
}