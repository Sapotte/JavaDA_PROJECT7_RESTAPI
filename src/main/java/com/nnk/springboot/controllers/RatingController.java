package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.CurveService;
import com.nnk.springboot.services.RatingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class RatingController {
    private static Logger logger = LogManager.getLogger(RatingController.class);

    private final RatingRepository ratingRepository;
    private final RatingService ratingService;
    private final CurveService curveService;

    public RatingController(RatingRepository ratingRepository, RatingService ratingService, CurveService curveService) {
        this.ratingRepository = ratingRepository;
        this.ratingService = ratingService;
        this.curveService = curveService;
    }

    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        try {
            var ratingList = ratingRepository.findAll();
            model.addAttribute("ratingList", ratingList);
            return "rating/list";
        } catch (Exception e) {
            logger.error(e);
            model.addAttribute("message", "An error occurred while retrieving the ratings");
            return "rating/list?error";
        }
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "curvePoint/add?errorValidation";
        }
        try {
            var newRating = ratingService.createRating(rating);
            model.addAttribute("newCurvePoint", newRating);
            return "rating/add";
        } catch (Exception e) {
            result.reject("error", "Error saving new curve point");
            return "rating/add?error";
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
            return "rating/list?error";
        }
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "curvePoint/update/{id}?errorValidation";
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
            curveService.deleteCurvePoint(id);
            return "redirect:/rating/list";
        } catch (Exception e) {
            logger.error(e);
            model.addAttribute("message", e.getMessage());
            return "redirect:/rating/list?errorDelete";
        }
    }
}
