package com.jsonplaceholder.communityservice.controllers;

import com.jsonplaceholder.communityservice.client.RestClient;
import com.jsonplaceholder.communityservice.model.Post;
import com.jsonplaceholder.communityservice.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;
    private final RestClient restClient;

    public PostController(PostService postService, RestClient restClient) {
        this.postService = postService;
        this.restClient = restClient;
    }

    @PostMapping
    public ResponseEntity<String> savePost(@RequestBody Post post) {
        post.setId(null);
        postService.savePost(post);
        return ResponseEntity.ok("The post has been successfully added. Please update the list with: http://localhost:8080/api/v1/posts");
    }

    @GetMapping
    public String getAllPosts(Model model) {
        List<Post> posts = postService.fetchAllPosts().getBody();
        model.addAttribute("posts", posts);
        return "posts";
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable Integer id, Model model) {
        ResponseEntity<Optional<Post>> postResponse = postService.fetchPostById(id);
        Post post = postResponse.getBody().get();
        model.addAttribute("posts", post);
        return "posts";
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePostById(@PathVariable Integer id, @RequestBody Post post) {
        ResponseEntity<?> updateUser = postService.updatePostById(id, post);
        if (updateUser.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("The post data of the user with id: " + id + " has been successfully updated. Please update the list with: http://localhost:8080/api/v1/posts");
        } else {
            return ResponseEntity.status(updateUser.getStatusCode()).body(updateUser.getBody().toString());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable Integer id) {
        postService.deletePostById(id);
        return ResponseEntity.ok("The post of the user with id: " + id + " has been successfully deleted. Please update the list with: http://localhost:8080/api/v1/posts");
    }

    @GetMapping("/external")
    public ResponseEntity<String> getExternalPosts() {
        Post[] posts = restClient.fetchExternalPosts();
        for (Post post : posts) {
            postService.savePost(post);
        }
        return ResponseEntity.ok("The posts has been successfully migrated. Please update the list with: http://localhost:8080/api/v1/posts");
    }

    @GetMapping("/external/{id}")
    public ResponseEntity<String> getExternalPostById(@PathVariable Integer id) {
        Post post = restClient.fetchExternalPostById(id);
        postService.savePost(post);
        return ResponseEntity.ok("The post of the user with id: " + id + " has been successfully migrated. Please update the list with: http://localhost:8080/api/v1/posts");
    }
}
