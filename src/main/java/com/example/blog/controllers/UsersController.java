package com.example.blog.controllers;

import com.example.blog.models.User;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class UsersController {

    private final UserRepository userRepository;

    @Autowired
    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public String allUsers(Model model){
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user/user-main";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user){
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationPost(@ModelAttribute("user") @Validated User user,
                                   BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "registration";
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}")
    public String userDetails(@PathVariable(value = "id") long id, Model model){
        if (!userRepository.existsById(id))
            return "redirect:/";
        Optional<User> user = userRepository.findById(id);
        ArrayList<User> res = new ArrayList<>();
        user.ifPresent(res::add);
        model.addAttribute("user", res);
        return "user/user-details";
    }
}
