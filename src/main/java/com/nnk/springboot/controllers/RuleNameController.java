package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
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

import jakarta.validation.Valid;

@Controller
public class RuleNameController {
    private static final Logger logger = LogManager.getLogger(RuleNameController.class);
    @Autowired
    private RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            var ruleNameList = ruleNameService.findAllRuleNames();
            model.addAttribute("ruleNames", ruleNameList);
            model.addAttribute("username", auth.getName());
            return "ruleName/list";
        } catch (Exception e) {
            logger.error(e);
            model.addAttribute("message", "An error occurred while retrieving the rule names");
            return "ruleName/list?error";
        }
    }

    @GetMapping("/ruleName/add")
    public ModelAndView addRuleForm() {
        ModelAndView mav = new ModelAndView("ruleName/add");
        mav.addObject("ruleName", new RuleName());
        return mav;
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid @ModelAttribute("ruleName") RuleName ruleName, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "ruleName/add";
        }
        try {
            var newRuleName = ruleNameService.createRuleName(ruleName);
            model.addAttribute("newRuleName", newRuleName);
            return "ruleName/list";
        } catch (Exception e) {
            result.reject("error", "Error saving new curve point");
            return "ruleName/add?error";
        }
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        var ruleName = ruleNameService.findRuleNameById(id);
        try {
            model.addAttribute("ruleName", ruleName);
            return "ruleName/update";
        } catch (Exception e) {
            logger.error(e.getMessage());
            model.addAttribute("message", e.getMessage());
            return "ruleName/list?errorUpdate";
        }
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "ruleName/update/{id}";
        }
        try {
            ruleNameService.updateRuleName(id, ruleName.getName(), ruleName.getDescription(), ruleName.getJson(), ruleName.getTemplate(), ruleName.getSqlStr(), ruleName.getSqlPart());
            return "redirect:/ruleName/list";
        } catch (Exception e) {
            result.reject("error", e.getMessage());
            return "ruleName/update/{id}?error";
        }
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        try {
            ruleNameService.deleteRuleName(id);
            return "redirect:/ruleName/list";
        } catch (Exception e) {
            logger.error(e);
            model.addAttribute("message", e.getMessage());
            return "redirect:/ruleName/list?errorDelete";
        }
    }
}
