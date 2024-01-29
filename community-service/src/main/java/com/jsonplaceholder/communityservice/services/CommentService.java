package com.jsonplaceholder.communityservice.services;

import com.jsonplaceholder.communityservice.model.Comment;
import com.jsonplaceholder.communityservice.model.Post;
import com.jsonplaceholder.communityservice.repositories.CommentRepository;
import com.jsonplaceholder.communityservice.repositories.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public void saveComment(Comment comment) {
        logger.info("Saving comment: {}", comment);
        Post post = postRepository.findById(comment.getPostId()).orElseThrow(() -> new EntityNotFoundException("Post not found"));
        comment.setPost(post);
        commentRepository.save(comment);
    }

    public ResponseEntity<List<Comment>> fetchAllComments() {
        logger.info("Fetching all comments");
        return ResponseEntity.ok(commentRepository.findAll());
    }

    public ResponseEntity<Optional<Comment>> fetchCommentById(Integer id) {
        logger.info("Fetching comment by ID: {}", id);
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            logger.info("Comment found: {}", comment.get());
            return ResponseEntity.ok(comment);
        } else {
            logger.warn("Comment not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> updateCommentById(Integer id, Comment updatedComment) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        logger.info("Updating comment by ID: {}", id);
        Comment existingComment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));

        if (updatedComment.getPostId() != null) {
            return ResponseEntity.badRequest().body("You can't change the post id of comment. Please try again");
        }
        if (updatedComment.getId() != null) {
            return ResponseEntity.badRequest().body("You can't change the comment id. Please try again");
        }
        if (updatedComment.getName() != null) {
            existingComment.setName(updatedComment.getName());
        }
        if (updatedComment.getEmail() != null) {
            existingComment.setEmail(updatedComment.getEmail());
        }
        if (updatedComment.getBody() != null) {
            existingComment.setBody(updatedComment.getBody());
        }

        Comment savedEntity = commentRepository.save(existingComment);
        logger.info("Comment updated successfully: {}", savedEntity);

        return ResponseEntity.ok(savedEntity);
    }

    public void deleteCommentById(Integer id) {
        logger.info("Deleting comment by ID: {}", id);
        commentRepository.deleteById(id);
    }

    public Comment findCommentByEmail(String email) {
        logger.info("Finding comment by email: {}", email);
        return commentRepository.findByEmail(email);
    }
}
