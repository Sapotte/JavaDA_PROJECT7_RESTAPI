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

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        try {
            var newBid = bidService.addBid(bid);
            model.addAttribute("newBid", newBid);
            return "bidList/list";
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
