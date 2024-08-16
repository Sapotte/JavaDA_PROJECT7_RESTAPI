package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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

import jakarta.validation.Valid;

@Controller
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @RequestMapping("/trade/list")
    public String home(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", auth.getName());
        try {
            model.addAttribute("trades", tradeService.findAllTrades());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public ModelAndView addUser(Trade trade) {
        ModelAndView mav = new ModelAndView("trade/add");
        mav.addObject("trade", trade);
        return mav;
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result) {
        if(result.hasErrors()) {
            return "trade/add";
        }
        try {
            tradeService.createTrade(trade);
            return "redirect:/trade/list";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("trade", tradeService.findTradeById(id));
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "trade/update";
        }
        try {
            tradeService.updateTrade(id, trade.getAccount(), trade.getType(), trade.getBuyQuantity());
            return "redirect:/trade/list";
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        try {
            tradeService.deleteTrade(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/trade/list";
    }
}
