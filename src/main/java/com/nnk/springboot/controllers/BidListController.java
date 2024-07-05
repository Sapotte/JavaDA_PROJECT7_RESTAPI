package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidService;
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

import javax.validation.Valid;


@Controller
public class BidListController {
    private static Logger logger = LogManager.getLogger(BidListController.class);
    @Autowired
    BidService bidService;

    @RequestMapping("/bidList/list")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", auth.getName());

        model.addAttribute("bidLists", bidService.getBidLists());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/add?errorValidation";
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
        model.addAttribute("bid", bid);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/update?errorValidation";
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
