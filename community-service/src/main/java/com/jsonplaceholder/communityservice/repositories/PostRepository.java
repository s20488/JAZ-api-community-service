package com.jsonplaceholder.communityservice.repositories;

import com.jsonplaceholder.communityservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
