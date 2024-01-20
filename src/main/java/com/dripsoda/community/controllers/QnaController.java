package com.dripsoda.community.controllers;

import com.dripsoda.community.entities.member.ContactCountryEntity;
import com.dripsoda.community.entities.member.UserEntity;
import com.dripsoda.community.services.AccompanyService;
import com.dripsoda.community.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "com.dripsoda.community.contollers.QnaController")
@RequestMapping(value = "/qna/")
public class QnaController {
    private final AccompanyService accompanyService;
    private final MemberService memberService;

    @Autowired

    public QnaController(AccompanyService accompanyService, MemberService memberService) {
        this.accompanyService = accompanyService;
        this.memberService = memberService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getIndex(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @RequestParam(value = "tab", required = false, defaultValue = "help") String tab,
            ModelAndView modelAndView) {
        if(tab == null || tab.equals("help") || (!tab.equals("event") && !tab.equals("qna") && !tab.equals("question"))) {
            modelAndView.addObject(ContactCountryEntity.ATTRIBUTE_NAME_PLURAL, this.memberService.getContactCountries());
        }
        modelAndView.setViewName("qna/index");
        return modelAndView;
    }
}
