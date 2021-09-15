package com.example.blog.controllers;

import com.example.blog.models.Comment;
import com.example.blog.models.Post;
import com.example.blog.models.User;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
public class CommentOfPostController {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentOfPostController(UserRepository userRepository,
                                   PostRepository postRepository,
                                   CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @PostMapping("blog/{id}/add-comment")
    public String addPostComment(@PathVariable(value = "id") long id,
                                 @RequestParam @Validated String text,
                                 Principal principal) {
        Comment comment = new Comment();
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Post post = postRepository.findById(id).orElseThrow();
        comment.setText(text);
        comment.setUser(user);
        comment.setPost(post);
        post.getComments().add(comment);
        commentRepository.save(comment);
        postRepository.save(post);
        return "redirect:/blog/" + id;
    }
}
