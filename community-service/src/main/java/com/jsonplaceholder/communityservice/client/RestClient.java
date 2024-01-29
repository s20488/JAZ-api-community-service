package com.jsonplaceholder.communityservice.client;

import com.jsonplaceholder.communityservice.model.Comment;
import com.jsonplaceholder.communityservice.model.Post;
import com.jsonplaceholder.communityservice.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
// Implementacja klienta do interakcji ze zdalną obsługą internetową przy użyciu żądań HTTP
public class RestClient {
    private static final String API_URL_USERS = "https://jsonplaceholder.typicode.com/users";
    private static final String API_URL_POSTS = "https://jsonplaceholder.typicode.com/posts";
    private static final String API_URL_COMMENTS = "https://jsonplaceholder.typicode.com/comments";

    public User[] fetchExternalUsers() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User[]> response = restTemplate.getForEntity(API_URL_USERS, User[].class);
        return response.getBody();
    }

    public User fetchExternalUserById(Integer id) {
        String apiUrl = API_URL_USERS + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> response = restTemplate.getForEntity(apiUrl, User.class);
        return response.getBody();
    }

    public Post[] fetchExternalPosts() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Post[]> response = restTemplate.getForEntity(API_URL_POSTS, Post[].class);
        return response.getBody();
    }

    public Post fetchExternalPostById(Integer id) {
        String apiUrl = API_URL_POSTS + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Post> response = restTemplate.getForEntity(apiUrl, Post.class);
        return response.getBody();
    }

    public Comment[] fetchExternalComments() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Comment[]> response = restTemplate.getForEntity(API_URL_COMMENTS, Comment[].class);
        return response.getBody();
    }

    public Comment fetchExternalCommentById(Integer id) {
        String apiUrl = API_URL_COMMENTS + "/" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Comment> response = restTemplate.getForEntity(apiUrl, Comment.class);
        return response.getBody();
    }

}