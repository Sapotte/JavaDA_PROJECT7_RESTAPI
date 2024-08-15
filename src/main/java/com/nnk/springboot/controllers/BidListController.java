package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BidListController {
    private static Logger logger = LogManager.getLogger(BidListController.class);
    @Autowired
    BidService bidService;

    /**
     * Retrieves the home page view for the bid list.

     * This method is mapped to the "/bidList/list" URL and is executed when a request with that URL is made.
     * It retrieves the user authentication information, adds it to the ModelAndView object along with the bid lists
     * retrieved from the bid service, and returns the ModelAndView object with the "bidList/list" view name.

     * @return A ModelAndView object representing the home page view for the bid list.
     */
    @RequestMapping("/bidList/list")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("bidList/list");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        mav.addObject("user", auth.getName());
        mav.addObject("bidLists", bidService.getBidLists());
        return mav;
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    /**
     * Validates a bid and returns the appropriate view.

     * This method is mapped to the "/bidList/validate" URL and is executed when a POST request with that URL is made.
     * It accepts a validated bid object, a BindingResult object to store validation errors, and a Model object to add
     * attributes for the view.

     * @param bid The validated bid object to be added.
     * @param result The BindingResult object to store validation errors.
     * @return The view name to be rendered based on the validation result.
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        try {
            bidService.addBid(bid);
            return "redirect:/bidList/list";
        } catch (Exception e) {
            logger.error(e);
            return "bidList/add?errorDB";
        }
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        var bid = bidService.getBidById(id);
        model.addAttribute("bidList", bid);
        return "bidList/update";
    }

    /**
     * Updates a bid list with the given ID.

     * This method is mapped to the "/bidList/update/{id}" URL and is executed when a POST request with that URL is made.
     * It accepts the ID of the bid list to be updated, a validated bid list object, a BindingResult object to store
     * validation errors, and a Model object to add attributes for the view.

     * @param id The ID of the bid list to be updated.
     * @param bidList The validated bid list object to be updated.
     * @param result The BindingResult object to store validation errors.
     * @param model The Model object to add attributes for the view.
     * @return The view name to be rendered based on the result of the bid list update.
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/update";
        }
        try {
            bidService.updateBid(id, bidList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "bidList/update?errorDB";
        }
        model.addAttribute("bidList", bidService.getBidLists());
        return "redirect:/bidList/list";
    }

    /**
     * Deletes a bid with the given ID.

     * This method is mapped to the "/bidList/delete/{id}" URL and is executed when a GET request with that URL is made.
     * It accepts the ID of the bid to be deleted and a Model object to add attributes for the view.

     * @param id The ID of the bid to be deleted.
     * @param model The Model object to add attributes for the view.
     * @return The view name to be rendered after deleting the bid.
     * @throws IllegalArgumentException if there is an error deleting the bid.
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        try {
            bidService.deleteBid(id);
            return "redirect:/bidList/list";
        } catch (Exception e) {
            logger.error(e.getMessage());
            model.addAttribute("message", e.getMessage());
            return "redirect:/bidList/list?errorDelete";
        }
    }
}
