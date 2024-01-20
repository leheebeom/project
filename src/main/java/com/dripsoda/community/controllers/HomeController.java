package com.dripsoda.community.controllers;

import com.dripsoda.community.dtos.accompany.ArticleRecentListDto;
import com.dripsoda.community.dtos.accompany.ArticleSearchDto;
import com.dripsoda.community.entities.accompany.ArticleEntity;
import com.dripsoda.community.entities.member.UserEntity;
import com.dripsoda.community.services.AccompanyService;
import com.dripsoda.community.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller(value = " com.dripsoda.www.controllers.HomeController")
@RequestMapping(value = "/")
public class HomeController {

    private final AccompanyService accompanyService;
    private final MemberService memberService;

    @Autowired
    public HomeController(AccompanyService accompanyService, MemberService memberService) {
        this.accompanyService = accompanyService;
        this.memberService = memberService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getIndex(@RequestAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                 ArticleEntity articleEntity,
                                 ArticleSearchDto articleSearchDto,
                                 ModelAndView modelAndView) {

//        List<ArticleRecentListDto> articleRecentListDto = this.accompanyService.getArticles();

        List<UserEntity> userList = this.memberService.getUsers();

        modelAndView.addObject(ArticleEntity.ATTRIBUTE_NAME_PLURAL, this.accompanyService.getArticles());

        modelAndView.setViewName("home/index");
        return modelAndView;
    }
}