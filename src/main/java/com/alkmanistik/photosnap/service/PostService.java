package com.alkmanistik.photosnap.service;

import com.alkmanistik.photosnap.exception.PostNotFoundException;
import com.alkmanistik.photosnap.model.Comment;
import com.alkmanistik.photosnap.model.Like;
import com.alkmanistik.photosnap.model.Post;
import com.alkmanistik.photosnap.model.User;
import com.alkmanistik.photosnap.repository.CommentRepository;
import com.alkmanistik.photosnap.repository.LikeRepository;
import com.alkmanistik.photosnap.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final StorageService storageService;

    public Post createPost(Authentication authentication,
                           String description,
                           MultipartFile image) {
        User author = userService.getCurrentUser(authentication);
        String imageUrl = storageService.store(image, "posts");

        Post post = Post.builder()
                .description(description)
                .imageUrl(imageUrl)
                .author(author)
                .build();

        return postRepository.save(post);
    }

    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findByAuthorIsPrivateFalseOrderByCreatedAtDesc(pageable);
    }

    public Post getPostById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
    }

    public Comment addComment(Authentication authentication,
                              UUID postId,
                              String text) {
        User user = userService.getCurrentUser(authentication);
        Post post = getPostById(postId);

        Comment comment = Comment.builder()
                .text(text)
                .author(user)
                .post(post)
                .build();

        return commentRepository.save(comment);
    }

    public void toggleLike(Authentication authentication, UUID postId) {

        User user = userService.getCurrentUser(authentication);
        Post post = getPostById(postId);

        if (likeRepository.existsByUserAndPost(user, post)) {
            likeRepository.deleteByUserAndPost(user, post);
        }else{
            Like like = Like.builder()
                    .user(user)
                    .post(post)
                    .build();

            likeRepository.save(like);
        }
    }
}
