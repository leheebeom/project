package com.dripsoda.community.controllers;

import com.dripsoda.community.services.AccompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = " com.dripsoda.www.controllers.HomeController")
@RequestMapping(value = "/")
public class    HomeController {

    private final AccompanyService accompanyService;
    @Autowired
    public HomeController(AccompanyService accompanyService) {
        this.accompanyService = accompanyService;
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("home/index");
        return modelAndView;
    }
}
