package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import com.nnk.springboot.utils.RegexValidation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;

    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @RequestMapping("/list")
    public ModelAndView home()
    {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("user/list");
        mav.addObject("users", userRepository.findAll());
        return mav;
    }

    @GetMapping("/add")
    public ModelAndView addUser() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("user/add");
        mav.addObject("user", new User());
        return mav;
    }

    /**
     * Validates the user object and performs additional validation on the password.
     * If the validation fails, adds appropriate error messages to the BindingResult object.
     * If the validation succeeds, adds the user object to the user repository and redirects to the user list page.
     *
     * @param user   the user object to validate
     * @param result the BindingResult object to add the errors to
     * @param model  the Model object to add attributes to
     * @return the view name to render, either "user/add" if there are errors, or "redirect:/user/list" if the validation succeeds
     */
    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        // If the password is not blank, check if it's valid and add an error to the validation if it's not
        if(user.getPassword() != null && !user.getPassword().trim().isBlank()) {
            RegexValidation.addErrorIfPasswordNotValid(result, user.getPassword());
        }
        if (result.hasErrors()) {
            return "user/add";
        }
        try {
            userService.addUser(user);
        } catch (Exception e) {
            // Add new error to validation if the username is already used
            if( e instanceof IllegalArgumentException) {
                result.addError(new FieldError("user", "username", "This userName already exists"));
                LOG.error("Username already exists", e);
                return "user/add";
            } else {
                return "user/add?error";
            }
        }
        return "redirect:/user/list";
    }

    @GetMapping("/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("user/update");
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        mav.addObject("user", user);
        return mav;
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        if(user.getPassword() != null && !user.getPassword().trim().isBlank()) {
            RegexValidation.addErrorIfPasswordNotValid(result, user.getPassword());
        }
        if (result.hasErrors()) {
            return "user/update";
        }
        try {
            userService.updateUser(user, id);
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException) {
                LOG.error("User not found", e);
                return "user/update?error";
            }
            LOG.error(e.getMessage(), e);
            throw e;
        }
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/user/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/user/list";
    }
}
