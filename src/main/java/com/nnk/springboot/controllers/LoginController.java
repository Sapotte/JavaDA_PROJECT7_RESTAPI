package com.nnk.springboot.controllers;

import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("app")
public class LoginController {

    private final UserRepository userRepository;

    private final BidListController bidListController;

    private final UserController userController;

    public LoginController(UserRepository userRepository, BidListController bidListController, UserController userController) {
        this.userRepository = userRepository;
        this.bidListController = bidListController;
        this.userController = userController;
    }

    /**
     * Retrieves the home page view based on the user authentication.
     *
     * This method is mapped to the "/home" URL and is executed when a request with that URL is made.
     * It retrieves the user authentication information, checks if the user has admin role, and then redirects
     * to the appropriate home page - either the userController's home page or the bidListController's home page.
     *
     * @return A ModelAndView object representing the home page view.
     */
    @GetMapping("/home")
    public ModelAndView home() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // redirect to admin page if user has admin role
        if(auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return userController.home();
        } else {
            return bidListController.home();
        }
    }


    /**
     * Retrieves all the articles for each user.
     *
     * This method is mapped to the "/secure/article-details" URL and is executed when a GET request with that URL is made.
     * It retrieves all the users from the UserRepository and stores them in the "users" attribute of the ModelAndView object.
     * The view name is set to "user/list".
     *
     * @return A ModelAndView object representing the view of the articles for each user.
     */
    @GetMapping("secure/article-details")
    public ModelAndView getAllUserArticles() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", userRepository.findAll());
        mav.setViewName("user/list");
        return mav;
    }

    /**
     * Retrieves a ModelAndView object representing the error page.
     *
     * This method is mapped to the "/error" URL and is executed when a GET request with that URL is made.
     * It creates a ModelAndView object, sets the error message to "You are not authorized for the requested data",
     * adds the error message as an attribute with the name "errorMsg", sets the view name to "403", and
     * returns the ModelAndView object.
     *
     * @return A ModelAndView object representing the error page view.
     */
    @GetMapping("error")
    public ModelAndView error() {
        ModelAndView mav = new ModelAndView();
        String errorMessage= "You are not authorized for the requested data.";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("403");
        return mav;
    }
}
