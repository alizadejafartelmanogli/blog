package com.example.blog.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class User extends Person {

    @Enumerated(value = EnumType.STRING)
    private Role role = Role.USER;

    @Enumerated(value = EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Post> posts;

    public User(String name, String email, String password) {
        super(name, email, password);
    }

    public User() {
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> userPosts) {
        this.posts = posts;
    }
}
