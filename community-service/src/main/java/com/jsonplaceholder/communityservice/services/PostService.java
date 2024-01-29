package com.jsonplaceholder.communityservice.services;

import com.jsonplaceholder.communityservice.model.Post;
import com.jsonplaceholder.communityservice.model.User;
import com.jsonplaceholder.communityservice.repositories.PostRepository;
import com.jsonplaceholder.communityservice.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void savePost(Post post) {
        logger.info("Saving post: {}", post);
        User user = userRepository.findById(post.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        post.setUser(user);
        postRepository.save(post);
    }

    public ResponseEntity<List<Post>> fetchAllPosts() {
        logger.info("Fetching all posts");
        return ResponseEntity.ok(postRepository.findAll());
    }

    public ResponseEntity<Optional<Post>> fetchPostById(Integer id) {
        logger.info("Fetching post by ID: {}", id);
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            logger.info("Post found: {}", post.get());
            return ResponseEntity.ok(post);
        } else {
            logger.warn("Post not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> updatePostById(Integer id, Post updatedPost) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        logger.info("Updating post by ID: {}", id);
        Post existingPost = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));

        if (updatedPost.getUserId() != null) {
            return ResponseEntity.badRequest().body("You can't change the user id of the post. Please try again");
        }
        if (updatedPost.getId() != null) {
            return ResponseEntity.badRequest().body("You can't change the post the id. Please try again");
        }
        if (updatedPost.getTitle() != null) {
            existingPost.setTitle(updatedPost.getTitle());
        }
        if (updatedPost.getBody() != null) {
            existingPost.setBody(updatedPost.getBody());
        }

        Post savedEntity = postRepository.save(existingPost);
        logger.info("Post updated successfully: {}", savedEntity);

        return ResponseEntity.ok(savedEntity);
    }

    public void deletePostById(Integer id) {
        logger.info("Deleting post by ID: {}", id);
        postRepository.deleteById(id);
    }
}
