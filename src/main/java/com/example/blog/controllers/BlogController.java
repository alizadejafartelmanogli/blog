package com.example.blog.controllers;

import com.example.blog.models.Post;
import com.example.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/blog")
public class BlogController {

    private final PostRepository postRepository;

    @Autowired
    public BlogController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public String blogMain(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blogs/blog-main";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('developers:write')")
    public String blogAdd(@ModelAttribute("post") Post post) {
        return "blogs/blog-add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('developers:write')")
    public String blogPostAdd(@ModelAttribute("post") @Validated Post post,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "blogs/blog-add";
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/{id}")
    public String blogDetails(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
        model.addAttribute("post", post);
        return "blogs/blog-details";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('developers:write')")
    public String blogEdit(Model model, @PathVariable("id") long id) {
        model.addAttribute("post", postRepository.findById(id).orElseThrow());
        return "blogs/blog-edit";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('developers:write')")
    public String blogPostUpdate(@PathVariable(value = "id") long id,
                                 @ModelAttribute("post") @Validated Post post,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "blogs/blog-edit";
        Post postOld = postRepository.findById(id).orElseThrow();
        postOld.setTittle(post.getTittle());
        postOld.setAnons(post.getAnons());
        postOld.setText(post.getText());
        postRepository.save(postOld);
        return "redirect:/blog";
    }
}

