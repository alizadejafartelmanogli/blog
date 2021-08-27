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
public class BlogController {

    private final PostRepository postRepository;

    @Autowired
    public BlogController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/blog")
    public String blogMain(Model model){
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog/blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(@ModelAttribute ("post") Post post){
        return "blog/blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@ModelAttribute ("post") @Validated Post post,
                              BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "blog/blog-add";
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") long id, Model model){
        if (!postRepository.existsById(id))
            return "redirect:/blog";
        Post post = postRepository.findById(id).orElseThrow();
        post.setViews(post.getViews()+1);
        postRepository.save(post);
        model.addAttribute("post", post);
        return "blog/blog-details";
    }

    @GetMapping("blog/{id}/edit")
    public String blogEdit(Model model, @PathVariable("id") long id) {
        model.addAttribute("post", postRepository.findById(id).orElseThrow());
        return "blog/blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long id,
                                 @ModelAttribute ("post") @Validated Post post,
                                 BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "blog/blog-edit";
        Post postOld = postRepository.findById(id).orElseThrow();
        postOld.setTittle(post.getTittle());
        postOld.setAnons(post.getAnons());
        postOld.setText(post.getText());
        postRepository.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    @PreAuthorize("hasAuthority('developers:write')")
    public String blogPostDelete(@PathVariable(value = "id") long id){
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "redirect:/blog";
    }
}

