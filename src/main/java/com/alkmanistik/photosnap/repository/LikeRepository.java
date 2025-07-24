package com.alkmanistik.photosnap.repository;

import com.alkmanistik.photosnap.model.Like;
import com.alkmanistik.photosnap.model.Post;
import com.alkmanistik.photosnap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    boolean existsByUserAndPost(User user, Post post);
    int countByPost(Post post);
    void deleteByUserAndPost(User user, Post post);
}