package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurveService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

@Controller
public class CurveController {
    private static Logger logger = LogManager.getLogger(CurveController.class);

    private final CurveService curveService;

    public CurveController(CurveService curveService) {
        this.curveService = curveService;
    }


    @RequestMapping("/curvePoint/list")
    public ModelAndView home(Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView mav = new ModelAndView("curvePoint/list");
        try {
            var curvePointList = curveService.findAllCurvePoints();
            mav.addObject("curvePoints", curvePointList);
            mav.addObject("username", auth.getName());
            return mav;
        } catch (Exception e) {
            logger.error(e);
            mav.addObject("message", "An error occurred while retrieving the curve points");
            return mav;
        }
    }

    @GetMapping("/curvePoint/add")
    public ModelAndView addCurvePointForm() {
        ModelAndView mav = new ModelAndView("curvePoint/add");
        mav.addObject("curvePoint", new CurvePoint());
        return mav;
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid @ModelAttribute("curvePoint") CurvePoint curvePoint, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "curvePoint/add";
        }
        try {
            var newCurvePoint = curveService.createCurvePoint(curvePoint);
            model.addAttribute("newCurvePoint", newCurvePoint);
            return "redirect:/curvePoint/list";
        } catch (Exception e) {
            result.reject("error", "Error saving new curve point");
            return "curvePoint/add?error";
        }

    }

    @GetMapping("/curvePoint/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Integer id, Model model) {
        ModelAndView mav = new ModelAndView("curvePoint/update");
            var curvePoint = curveService.findCurvePointById(id);
            try {
                mav.addObject("curvePoint", curvePoint);
                return mav;
            } catch (Exception e) {
                logger.error(e.getMessage());
                mav.addObject("message", e.getMessage());
                return mav;
        }
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurve(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                              BindingResult result) {
        if(result.hasErrors()) {
            return "curvePoint/update/{id}";
        }
        try {
            curveService.updateCurvePoint(id, curvePoint.getTerm(), curvePoint.getValue());
            return "redirect:/curvePoint/list";
        } catch (Exception e) {
            result.reject("error", e.getMessage());
            return "curvePoint/update/{id}";
        }
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurve(@PathVariable("id") Integer id, Model model) {
        try {
            curveService.deleteCurvePoint(id);
            return "redirect:/curvePoint/list";
        } catch (Exception e) {
            logger.error(e);
            model.addAttribute("message", e.getMessage());
            return "redirect:/curvePoint/list?errorDelete";
        }
    }
}
