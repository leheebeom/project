package com.dripsoda.community.controllers;

import com.dripsoda.community.dtos.accompany.ArticleBestTravleDto;
import com.dripsoda.community.dtos.accompany.ArticleSearchDto;
import com.dripsoda.community.entities.accompany.ArticleEntity;
import com.dripsoda.community.entities.member.ChatEntity;
import com.dripsoda.community.entities.member.UserEntity;
import com.dripsoda.community.services.AccompanyService;
import com.dripsoda.community.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
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
    public ModelAndView getIndex(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                 ArticleEntity articleEntity,
                                 ArticleSearchDto articleSearchDto,
                                 ModelAndView modelAndView) {

        String userEmail = (user != null) ? user.getEmail() : null;

        if(user != null) {
            modelAndView.addObject("profileImg", this.memberService.getUser(userEmail));
        }

        modelAndView.setViewName("home/index");

        List<ArticleBestTravleDto> articleRecentList = this.accompanyService.getArticlesForRecent();

        List<ChatEntity> allChatList = this.memberService.getChatsAll(userEmail);

        List<ChatEntity> chatList = this.memberService.chatUserCheckList(userEmail);

        List<ChatEntity> adminChatList = this.memberService.chatAdminCheckList(userEmail);

        List<UserEntity> userList = this.memberService.getUsers();
        int articleTotalCount = this.accompanyService.getCountArticles();

        modelAndView.addObject("userChat", chatList);

        //후에 해당 사용자에 맞는 관리자 답 보여주기.
        modelAndView.addObject("allChat", allChatList);

        modelAndView.addObject("adminChat", adminChatList);

        modelAndView.addObject("articleTotalCount", articleTotalCount);

        modelAndView.addObject("recentArticles", articleRecentList);


        modelAndView.addObject(UserEntity.ATTRIBUTE_NAME_PLURAL, userList);

        modelAndView.addObject(ArticleEntity.ATTRIBUTE_NAME_PLURAL, this.accompanyService.getArticles());

        return modelAndView;

    }
}