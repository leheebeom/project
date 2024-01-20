package com.dripsoda.community.controllers;

import com.dripsoda.community.dtos.accompany.ArticleSearchDto;
import com.dripsoda.community.entities.accompany.*;
import com.dripsoda.community.entities.member.UserEntity;
import com.dripsoda.community.enums.CommonResult;
import com.dripsoda.community.enums.accompany.CommentResult;
import com.dripsoda.community.interfaces.IResult;
import com.dripsoda.community.services.AccompanyService;
import com.dripsoda.community.services.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
@Controller(value = "com.dripsoda.community.controllers.AccompanyController")
@RequestMapping(value = "/accompany")
public class AccompanyController {
    private final AccompanyService accompanyService;
    private final MemberService memberService;

    @Autowired
    public AccompanyController(AccompanyService accompanyService, MemberService memberService) {
        this.accompanyService = accompanyService;
        this.memberService = memberService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("accompany/index");
        return modelAndView;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postIndex(@RequestParam(value = "lastArticleId") int lastArticleId,
                            RegionEntity region) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject responseJson = new JSONObject();
        ArticleSearchDto[] articles = this.accompanyService.searchArticles(region, lastArticleId);
        for (ArticleSearchDto article : articles) {
            article.setContent(article.getContent()
                    .replaceAll("<[^>]*>", "")
                    .replaceAll("&[^;]*;", ""));
//            <어쩌고> 형태를 다지워줌. 문제, &lt; &gt; &nbsp; 같은건 그대로 들어남.
        }
        responseJson.put(ArticleEntity.ATTRIBUTE_NAME_PLURAL, new JSONArray(objectMapper.writeValueAsString(articles)));
        return responseJson.toString();
    }

    @RequestMapping(value = "/", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchIndex() {
        // ObjectMapper mapper = new ObjectMapper();
        // JSONArray continentsJson = new JSONArray(mapper.writeValueAsString(this.accompanyService.getContinents()));
        // JSONArray countriesJson = new JSONArray(mapper.writeValueAsString(this.accompanyService.getCountries()));
        // JSONArray regionsJson = new JSONArray(mapper.writeValueAsString(this.accompanyService.getRegions()));
        // JSONObject responseJson = new JSONObject();
        // responseJson.put(ContinentEntity.ATTRIBUTE_NAME_PLURAL, continentsJson);
        // responseJson.put(CountryEntity.ATTRIBUTE_NAME_PLURAL, countriesJson);
        // responseJson.put(RegionEntity.ATTRIBUTE_NAME_PLURAL, regionsJson);
        // return responseJson.toString();

        JSONArray continentsJson = new JSONArray();
        for (ContinentEntity continent : this.accompanyService.getContinents()) {
            JSONObject continentJson = new JSONObject();
            continentJson.put("value", continent.getValue());
            continentJson.put("text", continent.getText());
            continentsJson.put(continentJson);
        }
        JSONArray countriesJson = new JSONArray();
        for (CountryEntity country : this.accompanyService.getCountries()) {
            JSONObject countryJson = new JSONObject();
            countryJson.put("continentValue", country.getContinentValue());
            countryJson.put("value", country.getValue());
            countryJson.put("text", country.getText());
            countriesJson.put(countryJson);
        }
        JSONArray regionsJson = new JSONArray();
        for (RegionEntity region : this.accompanyService.getRegions()) {
            JSONObject regionJson = new JSONObject();
            regionJson.put("continentValue", region.getContinentValue());
            regionJson.put("countryValue", region.getCountryValue());
            regionJson.put("value", region.getValue());
            regionJson.put("text", region.getText());
            regionsJson.put(regionJson);
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(ContinentEntity.ATTRIBUTE_NAME_PLURAL, continentsJson);
        responseJson.put(CountryEntity.ATTRIBUTE_NAME_PLURAL, countriesJson);
        responseJson.put(RegionEntity.ATTRIBUTE_NAME_PLURAL, regionsJson);
        return responseJson.toString();
    }

    //
    @RequestMapping(value = "cover-image/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getCoverImage(
            @PathVariable(value = "id") int id) {
        ArticleEntity article = this.accompanyService.getArticle(id);
        if (article == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String[] mimeArray = article.getCoverImageMime().split("/");
        String mimeType = mimeArray[0];
        String mimeSubType = mimeArray[1];
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(article.getCoverImage().length);
        headers.setContentType(new MediaType(mimeType, mimeSubType, StandardCharsets.UTF_8));
        return new ResponseEntity<>(article.getCoverImage(), headers, HttpStatus.OK);
    }


    @RequestMapping(value = "write", method = RequestMethod.GET)
    public ModelAndView getWrite(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            ModelAndView modelAndView
    ) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        }
        modelAndView.setViewName("accompany/write");
        return modelAndView;
    }

    @RequestMapping(value = "write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWrite(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
            @RequestParam(value = "coverImageFile") MultipartFile coverImageFile,
            @RequestParam(value = "dateFromStr") String dateFromStr,
            @RequestParam(value = "dateToStr") String dateToStr,
            ArticleEntity article
    ) throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        article.setIndex(-1)
                .setUserEmail(user.getEmail())
                .setCreatedAt(new Date())
                .setCoverImage(coverImageFile.getBytes())
                .setCoverImageMime(coverImageFile.getContentType())
                .setDateFrom(dateFormat.parse(dateFromStr))
                .setDateTo(dateFormat.parse(dateToStr));
        IResult result = this.accompanyService.putArticle(article);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("id", article.getIndex());
        }
        return responseJson.toString();
    }


    //    다운로드용 맵핑
    @RequestMapping(value = "image/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImage(@PathVariable(value = "id") int id) {
        ImageEntity image = this.accompanyService.getImage(id);
        if (image == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        String[] mimeArray = image.getMime().split("/");
        String mimeType = mimeArray[0];
        String mimeSubType = mimeArray[1];
        headers.setContentLength(image.getData().length);
        headers.setContentType(new MediaType(mimeType, mimeSubType, StandardCharsets.UTF_8));
        return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "image", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postImage(
            @RequestParam(value = "upload") MultipartFile upload,
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user
    ) throws IOException {
        ImageEntity image = ImageEntity.build()
                .setUserEmail(user.getEmail())
                .setCreatedAt(new Date())
                .setName(upload.getOriginalFilename())
                .setMime(upload.getContentType())
                .setData(upload.getBytes());
        IResult result = this.accompanyService.uploadImage(image);
        JSONObject responseJson = new JSONObject();

        if (result == CommonResult.SUCCESS) {
            responseJson.put("url", String.format("http://localhost:8080/accompany/image/%d", image.getIndex()));
        } else {
            JSONObject errorJson = new JSONObject();
            errorJson.put("message", "이미지 업로드에 실패하였습니다. 잠시 후 다시 시도해 주세요.");
            responseJson.put("error", errorJson);
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "read/{id}", method = RequestMethod.GET)
    public ModelAndView getRead(@PathVariable(value = "id") Integer id,
                                ModelAndView modelAndView,
                                HttpServletResponse response,
                                HttpServletRequest request) throws Exception {
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
                this.accompanyService.updateViews(id);
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);                            // 쿠키 시간
                response.addCookie(oldCookie);
            }
        } else {
            this.accompanyService.updateViews(id);
            Cookie newCookie = new Cookie("postView", "[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);                                // 쿠키 시간
            response.addCookie(newCookie);
        }
        //댓글 보여지는 화면
        modelAndView.addObject("articleId", id);
        modelAndView.addObject(CommentEntity.ATTRIBUTE_NAME_PLURAL, this.accompanyService.getCommentsByArticleIndex(id));
        modelAndView.setViewName("accompany/read");

        return modelAndView;
    }

    @RequestMapping(value = "read/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRead(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @PathVariable(value = "id") int id,
            HttpServletResponse response) throws JsonProcessingException, IOException {
        ArticleEntity article = this.accompanyService.getArticle(id);
        if (article == null) {
            response.setStatus(404);
            return null;
        }
        article.setCoverImage(null)
                .setCoverImageMime(null);
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject responseJson = new JSONObject(objectMapper.writeValueAsString(article));
        UserEntity articleUser = this.memberService.getUser(article.getUserEmail());
        responseJson.put("userNickname", articleUser.getNickname());
        responseJson.put("mine", user != null && (user.isAdmin() || user.equals(articleUser)));
        return responseJson.toString();
    }

    @RequestMapping(value = "read/{id}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentEntity>> getComment(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @PathVariable(value = "id") Integer id
    ) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<CommentEntity> comments = this.accompanyService.getAllCommentsForArticle(id);

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(comments, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "read/{id}/comments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postComment(@PathVariable(value = "id") Integer id,
                              @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                              HttpServletResponse response,
                              @ModelAttribute("new Comment") CommentEntity newComment) throws JsonProcessingException {
        // 댓글 작성을 처리하기 위한 서비스 메소드 호출
        ArticleEntity article = this.accompanyService.getArticle(id);
        // 게시글 값이 널이면 404
        if(article == null) {
            response.setStatus(404);
            return null;
        }
        //comment 객체 빌드 - id 값은 path의 id 값, 세션의 유저 이메일 값. 만약 session의 유저 값이 없으면 로그인 화면 - 로그인 하고 오라고 하기,

        //reply 이 있으면 parentIndex 값이 commentIndex , 없으면 parentIndex 값이 null
        IResult result = accompanyService.createComment(user,id,newComment);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());

        if(result == CommonResult.SUCCESS) {
            System.out.println("값 석세스임 이제");
            responseJson.put("articleId", newComment.getArticleIndex());
            responseJson.put("id", newComment.getIndex());

        }
        // 댓글을 작성한 후 동일한 읽기 페이지로 리다이렉트
        return responseJson.toString();
    }

    @RequestMapping(value = "read/{id}/reply/{commentId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postReply(@PathVariable(value = "id") Integer id,
                            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                              HttpServletResponse response,
                            @PathVariable(value = "commentId") Integer commentId,
                            @ModelAttribute("newReply") CommentEntity newReply) throws JsonProcessingException {
        // 댓글 작성을 처리하기 위한 서비스 메소드 호출
        ArticleEntity article = this.accompanyService.getArticle(id);
        CommentEntity comment = this.accompanyService.getCommentIndex(commentId);
        // 게시글 값이 널이면 404
        if(article == null) {
            response.setStatus(404);
            return null;
        }
        if(comment == null) {
            response.setStatus(404);
            return null;
        }
        IResult result = accompanyService.replyComment(user,id,commentId,newReply);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }


    @RequestMapping(value = "read/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteRead(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                             @PathVariable(value = "id") int id) {
        JSONObject responseJson = new JSONObject();
        ArticleEntity article = this.accompanyService.getArticle(id);
        if (article == null) {
            responseJson.put(IResult.ATTRIBUTE_NAME, CommonResult.FAILURE);
            return responseJson.toString();
        }
        if (user == null || !user.isAdmin() && !user.getEmail().equals(article.getUserEmail())) {
            responseJson.put(IResult.ATTRIBUTE_NAME, "k");
            return responseJson.toString();
        }
        IResult result = this.accompanyService.deleteArticle(id);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "modify/{id}", method = RequestMethod.GET)
    public ModelAndView getModify(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @PathVariable(value = "id") int id,
            ModelAndView modelAndView
    ) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        }
        modelAndView.setViewName("accompany/modify");
        return modelAndView;
    }

    @RequestMapping(value = "modify/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchModify(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                              @PathVariable(value = "id") int id,
                              HttpServletResponse response) throws JsonProcessingException {
        ArticleEntity article = this.accompanyService.getArticle(id);
        if (article == null) {
            response.setStatus(404);
            return null;
        }
        if (user == null || !user.isAdmin() && !user.getEmail().equals(article.getUserEmail())) {
            response.setStatus(403);
            return null;
        }
        article.setCoverImage(null)
                .setCoverImageMime(null);
        return new ObjectMapper().writeValueAsString(article);
    }

    @RequestMapping(value = "modify/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postModify(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
            @PathVariable(value = "id") int id,
            @RequestParam(value = "coverImageFile", required = false) MultipartFile coverImageFile,
            @RequestParam(value = "dateFromStr") String dateFromStr,
            @RequestParam(value = "dateToStr") String dateToStr,
            ArticleEntity article
    ) throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        article.setIndex(id)
                .setUserEmail(user.getEmail())
                .setCoverImage(coverImageFile == null ? null : coverImageFile.getBytes())
                .setCoverImageMime(coverImageFile == null ? null : coverImageFile.getContentType())
                .setDateFrom(dateFormat.parse(dateFromStr))
                .setDateTo(dateFormat.parse(dateToStr));
        IResult result = this.accompanyService.modifyArticle(article);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "request/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getRequest(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @PathVariable(value = "id") int id
    ) {
        JSONObject responseJson = new JSONObject();
        if (user == null) {
            responseJson.put(IResult.ATTRIBUTE_NAME, false);
        } else {
            responseJson.put(IResult.ATTRIBUTE_NAME, this.accompanyService.checkRequest(user, id));
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "request/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRequest(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
            @PathVariable(value = "id") int id
    ) {
        JSONObject responseJson = new JSONObject();
        IResult result = this.accompanyService.putRequest(user, id);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }


}