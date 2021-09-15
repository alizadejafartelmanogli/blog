package com.example.blog.controllers;

import com.example.blog.models.Comment;
import com.example.blog.models.Post;
import com.example.blog.models.User;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
public class PostsAndUserController {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public PostsAndUserController(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @PostMapping("/blog/{id}/add_to_me")
    public String postAddToUser(@PathVariable(value = "id") long id, Principal principal) {
        Post post = postRepository.findById(id).orElseThrow();
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        user.getPosts().add(post);
        post.getUsers().add(user);
        userRepository.save(user);
        return "redirect:/blog";
    }

    @GetMapping("/my_posts")
    public String userPosts(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        model.addAttribute("user", user);
        return "users/user-posts";
    }


    @PostMapping("/my_posts/{id}/delete")
    public String userPostsDelete(@PathVariable("id") long id, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Post post = postRepository.findById(id).orElseThrow();
        user.getPosts().remove(post);
        userRepository.save(user);
        return "redirect:/my_posts";
    }

    @GetMapping("blog/{id}/edit/delete")
    @PreAuthorize("hasAuthority('developers:write')")
    public String blogDelete(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        model.addAttribute("post", post);
        return "blogs/blog-delete";
    }

    @PostMapping("blog/{id}/edit/delete")
    @PreAuthorize("hasAuthority('developers:write')")
    public String blogPostDelete(@PathVariable(value = "id") long id) {
        Post post = postRepository.findById(id).orElseThrow();
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            user.getPosts().remove(post);
        }
        List<Comment> commentList = post.getComments();
        for (Comment comment : commentList) {
            commentRepository.delete(comment);
        }
        postRepository.delete(post);
        return "redirect:/blog";
    }
}
