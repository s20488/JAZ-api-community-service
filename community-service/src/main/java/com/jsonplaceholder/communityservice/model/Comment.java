package com.jsonplaceholder.communityservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "post_id", insertable = false, updatable = false)
    private Integer postId;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    private String name;
    private String email;
    private String body;
}
