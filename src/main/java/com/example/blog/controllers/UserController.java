package com.example.blog.controllers;

import com.example.blog.models.Post;
import com.example.blog.models.User;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String allUsers(Model model){
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users/user-main";
    }

    @GetMapping("/{id}")
    public String userDetails(@PathVariable(value = "id") long id, Model model){
        if (!userRepository.existsById(id))
            return "redirect:/";
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "users/user-details";
    }
}
