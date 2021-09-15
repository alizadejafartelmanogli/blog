package com.example.blog.models;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.List;


@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Tittle should not be empty")
    private String tittle;
    @NotEmpty(message = "Anons should not be empty")
    private String anons;
    @NotEmpty(message = "Text should not be empty")
    @Length(min = 2, max = 500, message = "Text should be between 10 and 500 characters")
    private String text;
    private int views;
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
    @ManyToMany
    private List<User> users;

    public Post(String tittle, String anons, String text) {
        this.tittle = tittle;
        this.anons = anons;
        this.text = text;
    }

    public Post() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getAnons() {
        return anons;
    }

    public void setAnons(String anons) {
        this.anons = anons;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> user) {
        this.users = users;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
