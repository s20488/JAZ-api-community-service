package com.jsonplaceholder.communityservice.controllers;

import com.jsonplaceholder.communityservice.client.RestClient;
import com.jsonplaceholder.communityservice.model.Comment;
import com.jsonplaceholder.communityservice.model.User;
import com.jsonplaceholder.communityservice.services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;
    private final RestClient restClient;

    public CommentController(CommentService commentService, RestClient restClient) {
        this.commentService = commentService;
        this.restClient = restClient;
    }

    @PostMapping
    public ResponseEntity<String> saveComment(@RequestBody Comment comment) {
        comment.setId(null);
        commentService.saveComment(comment);
        return ResponseEntity.ok("The comment has been successfully added. Please update the list with: http://localhost:8080/api/v1/comments");
    }

    @GetMapping
    public String getAllComments(Model model) {
        List<Comment> comments = commentService.fetchAllComments().getBody();
        model.addAttribute("comments", comments);
        return "comments";
    }

    @GetMapping("/{id}")
    public String getCommentById(@PathVariable Integer id, Model model) {
        ResponseEntity<Optional<Comment>> commentResponse = commentService.fetchCommentById(id);
        Comment comment = commentResponse.getBody().get();
        model.addAttribute("comments", comment);
        return "comments";
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCommentById(@PathVariable Integer id, @RequestBody Comment comment) {
        ResponseEntity<?> updateComment = commentService.updateCommentById(id, comment);
        if (updateComment.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("The comment data of the post with id: " + id + " has been successfully updated. Please update the list with: http://localhost:8080/api/v1/comments");
        } else {
            return ResponseEntity.status(updateComment.getStatusCode()).body(updateComment.getBody().toString());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommentById(@PathVariable Integer id) {
        commentService.deleteCommentById(id);
        return ResponseEntity.ok("The comment of the post with id: " + id + " has been successfully deleted. Please update the list with: http://localhost:8080/api/v1/comments");
    }

    @GetMapping("/findCommentByEmail/{email}")
    public String findUserByUsername(@PathVariable String email, Model model) {
        Comment comment = commentService.findCommentByEmail(email);
        if (comment != null) {
            model.addAttribute("comments", Collections.singletonList(comment));
        } else {
            model.addAttribute("comments", Collections.emptyList());
        }
        return "comments";
    }

    @GetMapping("/external")
    public ResponseEntity<String> getExternalPosts() {
        Comment[] comments = restClient.fetchExternalComments();
        for (Comment comment : comments) {
            commentService.saveComment(comment);
        }
        return ResponseEntity.ok("The comments has been successfully migrated. Please update the list with: http://localhost:8080/api/v1/comments");
    }

    @GetMapping("/external/{id}")
    public ResponseEntity<String> getExternalCommentById(@PathVariable Integer id) {
        Comment comment = restClient.fetchExternalCommentById(id);
        commentService.saveComment(comment);
        return ResponseEntity.ok("The comment of the post with id: " + id + " has been successfully migrated. Please update the list with: http://localhost:8080/api/v1/comments");
    }
}
