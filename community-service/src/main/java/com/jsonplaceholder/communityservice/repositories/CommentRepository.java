package com.jsonplaceholder.communityservice.repositories;

import com.jsonplaceholder.communityservice.model.Comment;
import com.jsonplaceholder.communityservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c WHERE c.email = :email")
    Comment findByEmail(@Param("email") String email);
}
