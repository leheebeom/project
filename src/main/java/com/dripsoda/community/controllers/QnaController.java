package com.dripsoda.community.controllers;

import com.dripsoda.community.entities.member.ContactCountryEntity;
import com.dripsoda.community.entities.member.UserEntity;
import com.dripsoda.community.entities.qna.QnaArticleEntity;
import com.dripsoda.community.services.AccompanyService;
import com.dripsoda.community.services.MemberService;
import com.dripsoda.community.services.QnaService;
import com.dripsoda.community.vos.qna.QnaArticleReadVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller(value = "com.dripsoda.community.contollers.QnaController")
@RequestMapping(value = "/qna/")
public class QnaController {
    private final AccompanyService accompanyService;
    private final MemberService memberService;
    private final QnaService qnaService;

    //qna 게시판은 작성 x 작성은 멤버에서 / 즉,get 그리고 read 요청시만 가능. / qnaController 의 기능 자체는 읽는 기능만 부여.
    @Autowired

    public QnaController(AccompanyService accompanyService, MemberService memberService, QnaService qnaService) {
        this.accompanyService = accompanyService;
        this.memberService = memberService;
        this.qnaService = qnaService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getIndex(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @RequestParam(value = "tab", required = false, defaultValue = "help") String tab,
            ModelAndView modelAndView,
            UserEntity userEntity) {
        if (tab == null || tab.equals("help") || (!tab.equals("event") && !tab.equals("qna"))) {
            modelAndView.addObject(ContactCountryEntity.ATTRIBUTE_NAME_PLURAL, this.memberService.getContactCountries());
        }
        modelAndView.setViewName("qna/index");
        List<QnaArticleEntity> helpQna = this.qnaService.getHelpQnaArticles();
        List<QnaArticleEntity> eventQna = this.qnaService.getEventQnaArticles();
        List<QnaArticleEntity> qnaQna = this.qnaService.getQnaArticles();
        modelAndView.addObject("helpQna", helpQna);
        modelAndView.addObject("eventQna", eventQna);
        modelAndView.addObject("qnaQna", qnaQna);
        return modelAndView;
    }
    //read 할떄 1번 - 공지사항 / 2번 이벤트 / 3번 qna로 select해서 read 하면됨.

    @RequestMapping(value = "read/{id}", method = RequestMethod.GET)
    public ModelAndView getRead(@PathVariable(value = "id") Integer id, ModelAndView modelAndView,
                                HttpServletResponse response,
                                HttpServletRequest request) {
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }
        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + id.toString() + "]")) {
                this.qnaService.updateViews(id);
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);                            // 쿠키 시간
                response.addCookie(oldCookie);
            }
        } else {
            this.qnaService.updateViews(id);
            Cookie newCookie = new Cookie("postView", "[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);                                // 쿠키 시간
            response.addCookie(newCookie);
        }

        int categoryId = this.qnaService.getCategoryId(id);
        QnaArticleReadVo QnaMovePage = this.qnaService.getHelpMove(id,categoryId);

        modelAndView.addObject("pageMove", QnaMovePage);
        modelAndView.addObject(QnaArticleEntity.ATTRIBUTE_NAME, this.qnaService.getArticle(id,categoryId));
        modelAndView.addObject("articleId", id);
//        modelAndView.addObject("helpNext",);
        modelAndView.setViewName("qna/read");
        return modelAndView;
    }

    @RequestMapping(value = "read/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRead(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @PathVariable(value = "id") int id,
            HttpServletResponse response) throws IOException {

        int categoryId = this.qnaService.getCategoryId(id);
        QnaArticleEntity qnaArticle = this.qnaService.getArticle(id,categoryId);
        if (qnaArticle == null) {
            response.setStatus(404);
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject responseJson = new JSONObject(objectMapper.writeValueAsString(qnaArticle));
        return responseJson.toString();
    }


}
