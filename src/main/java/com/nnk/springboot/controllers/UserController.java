package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import com.nnk.springboot.utils.RegexValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @RequestMapping("/user/list")
    public ModelAndView home()
    {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("user/list");
        mav.addObject("users", userRepository.findAll());
        return mav;
    }

    @GetMapping("/user/add")
    public ModelAndView addUser() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("user/add");
        mav.addObject("user", new User());
        return mav;
    }

    @PostMapping("/user/validate")
    public String validate(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        result = RegexValidation.addErrorIfPasswordNotValid(result, user.getPassword());
        if (result.hasErrors()) {
            return "user/add";
        }
        try {
            userService.addUser(user);
        } catch (Exception e) {
            if( e instanceof IllegalArgumentException) {
                result.addError(new FieldError("user", "username", "This userName already exists"));
                return "user/add";
            } else {
                return "user/add?error";
            }
        }
        userService.addUser(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/user/list";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        result = RegexValidation.addErrorIfPasswordNotValid(result, user.getPassword());
        if (result.hasErrors()) {
            return "user/update";
        }
        userService.updateUser(user, id);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/user/list";
    }
}
