package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RatingController {
    private static Logger logger = LogManager.getLogger(RatingController.class);

    private final RatingRepository ratingRepository;

    private final RatingService ratingService;

    public RatingController(RatingRepository ratingRepository, RatingService ratingService) {
        this.ratingRepository = ratingRepository;
        this.ratingService = ratingService;
    }

    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            var ratingList = ratingRepository.findAll();
            model.addAttribute("ratings", ratingList);
            model.addAttribute("username", auth.getName());
            return "rating/list";
        } catch (Exception e) {
            logger.error(e);
            model.addAttribute("message", "An error occurred while retrieving the ratings");
            return "rating/list?error";
        }
    }

    @GetMapping("/rating/add")
    public ModelAndView addRatingForm(Rating rating) {
        ModelAndView mav = new ModelAndView("rating/add");
        mav.addObject("rating", rating);
        return mav;
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid @ModelAttribute("rating") Rating rating, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "rating/add";
        }
        try {
            ratingService.createRating(rating);
            return "redirect:/rating/list";
        } catch (Exception e) {
            result.reject("error", "Error saving new curve point");
            return "rating/add";
        }
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        var rating = ratingRepository.findById(id);
        if(rating.isPresent()) {
            model.addAttribute("rating", rating.get());
            return "rating/update";
        } else {
           logger.error("Rating with id " + id + " not found");
           model.addAttribute("message", "Rating not found");
            return "rating/list";
        }
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "rating/update/{id}";
        }
        try {
            ratingService.updateRating(id, rating.getMoodysRating(), rating.getSandPRating(), rating.getFitchRating(), rating.getOrderNumber());
            return "redirect:/rating/list";
        } catch (Exception e) {
            result.reject("error", e.getMessage());
            return "rating/update/{id}?error";
        }

    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        try {
            ratingService.deleteRating(id);
            return "redirect:/rating/list";
        } catch (Exception e) {
            logger.error(e);
            model.addAttribute("message", e.getMessage());
            return "redirect:/rating/list?errorDelete";
        }
    }
}
