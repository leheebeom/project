package com.dripsoda.community.controllers;

import com.dripsoda.community.dtos.accompany.ArticleSearchDto;
import com.dripsoda.community.entities.accompany.*;
import com.dripsoda.community.entities.member.UserEntity;
import com.dripsoda.community.enums.CommonResult;
import com.dripsoda.community.enums.accompany.CommentResult;
import com.dripsoda.community.exceptions.RollbackException;
import com.dripsoda.community.interfaces.IResult;
import com.dripsoda.community.services.AccompanyService;
import com.dripsoda.community.services.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@Controller(value = "com.dripsoda.community.controllers.AccompanyController")
@RequestMapping(value = "/accompany")
public class AccompanyController {
    private final AccompanyService accompanyService;
    private final MemberService memberService;
    private static final Logger logger = LoggerFactory.getLogger(AccompanyController.class);

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
//            <어쩌고> 형태를 다지워줌. 문제, &lt; &gt; &nbsp; 같은건 그대로 들어남. -> 타임리프 utext 사용
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

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ModelAndView getSearch(ModelAndView modelAndView,
                                  @RequestParam(value = "keyword", required = false) String keyword) {

        if (keyword != null) {
            modelAndView.addObject("articlesForKeyword", this.accompanyService.getArticlesForKeyword(keyword));
            modelAndView.addObject("keyword", keyword);
        }
        modelAndView.setViewName("accompany/search");
        return modelAndView;
    }

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
        if (user == null || Objects.equals(user.getStatusValue(), "SUS")) {
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
                                @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
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
        //좋아요 판별.
        if (user != null) {
            List<CommentLikeEntity> commentLikeEntity = this.accompanyService.getCommentLikeByUserEmail(user.getEmail());
            //13번에 관한 코멘트 라이크 엔티티가 만들어 졋음.
            modelAndView.addObject(CommentLikeEntity.ATTRIBUTE_NAME_PLURAL, commentLikeEntity);
            if (commentLikeEntity != null && !commentLikeEntity.isEmpty()) {
                List<Integer> likedCommentIndices = new ArrayList<>();
                // 좋아요를 누른 댓글의 인덱스를 추출하여 리스트에 추가
                for (CommentLikeEntity likeEntity : commentLikeEntity) {
                    likedCommentIndices.add(likeEntity.getCommentIndex());
                }
                // 모델에 추가
                modelAndView.addObject("likedCommentIndices", likedCommentIndices);
            }
        }
        //댓글 보여지는 화면
//        modelAndView.addObject("likedComments", likedComments);
        modelAndView.addObject("articleId", id);
        //ex 2번 글이면 2번글에 대한 모든 댓글 조회
        modelAndView.addObject(CommentEntity.ATTRIBUTE_NAME_PLURAL, this.accompanyService.getArticleCommentsByArticleIndex(id));
        modelAndView.addObject(ArticleEntity.ATTRIBUTE_NAME, this.accompanyService.getArticleForUserProfile(id));
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

    //읽는 페이지에서 댓글수정이 이루어져야됨.
    @RequestMapping(value = "read/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchRead(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                            @PathVariable(value = "id") Integer id,
                            HttpServletResponse response,
                            @RequestParam(value = "commentIndex", required = false) Integer commentIndex,
                            @RequestParam(value = "content", required = false) String content) throws JsonProcessingException {
        ArticleEntity article = this.accompanyService.getArticle(id);
        CommentEntity comment = this.accompanyService.getCommentIndex(commentIndex);
        if (article == null) {
            response.setStatus(404);
            return null;
        }
        if (comment == null) {
            response.setStatus(404);
            return null;
        }
        if (user == null || Objects.equals(user.getStatusValue(), "SUS") && !user.getEmail().equals(comment.getUserEmail())) {
            response.setStatus(403);
            return null;
        }
        return new ObjectMapper().writeValueAsString(comment);
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
        if (user == null || Objects.equals(user.getStatusValue(), "SUS") && !user.getEmail().equals(article.getUserEmail())) {
            responseJson.put(IResult.ATTRIBUTE_NAME, "k");
            return responseJson.toString();
        }
        IResult result = this.accompanyService.deleteArticle(id);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    //관리자 볼 수 있는 전체 댓글 http로 던짐.
    @RequestMapping(value = "read/{id}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentEntity>> getComment(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @PathVariable(value = "id") Integer id
    ) {
        if (user == null || Objects.equals(user.getStatusValue(), "SUS") && !user.isAdmin()) {
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
        if (article == null) {
            response.setStatus(404);
            return null;
        }
        //comment 객체 빌드 - id 값은 path의 id 값, 세션의 유저 이메일 값. 만약 session의 유저 값이 없으면 로그인 화면 - 로그인 하고 오라고 하기,
        //reply 이 있으면 parentIndex 값이 commentIndex , 없으면 parentIndex 값이 null
        IResult result = accompanyService.createComment(user, id, newComment);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
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
        if (article == null) {
            response.setStatus(404);
            return null;
        }
        if (comment == null) {
            response.setStatus(404);
            return null;
        }
        IResult result = accompanyService.replyComment(user, id, commentId, newReply);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "modify/{id}/{commentId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String commentModify(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
                                @PathVariable(value = "id") Integer id,
                                @PathVariable(value = "commentId") Integer commentId,
                                CommentEntity comment) {
        comment.setIndex(commentId)
                .setArticleIndex(id)
                .setUserEmail(user.getEmail());
        IResult result = this.accompanyService.modifyComment(comment);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "delete/{id}/{commentId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteComment(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                @PathVariable(value = "id") Integer id,
                                @PathVariable(value = "commentId") Integer commentId) {
        JSONObject responseJson = new JSONObject();
        CommentEntity comment = this.accompanyService.getCommentIndex(commentId);
        if (comment == null) {
            responseJson.put(CommonResult.ATTRIBUTE_NAME, CommentResult.NOT_FOUND);
            return responseJson.toString();
        }
        if (user == null || Objects.equals(user.getStatusValue(), "SUS") && !user.getEmail().equals(comment.getUserEmail())) {
            responseJson.put(CommentResult.ATTRIBUTE_NAME, CommentResult.NOT_SIGNED);
            return responseJson.toString();
        }
        IResult result = this.accompanyService.deleteComment(id, commentId);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    //좋아요
    @RequestMapping(value = "like/{id}/{commentId}", method = RequestMethod.PUT)
    @ResponseBody
    public String putLikeComment(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                 @PathVariable(value = "id") Integer id,
                                 @PathVariable(value = "commentId") Integer commentId,
                                 CommentLikeEntity commentLike,
                                 HttpServletResponse response,
                                 HttpSession session
    ) {
        JSONObject responseJson = new JSONObject();
        ArticleEntity article = this.accompanyService.getArticle(id);
        CommentEntity comment = this.accompanyService.getCommentIndex(commentId);

        // 세션에 좋아요 정보 저장

        if (user == null || Objects.equals(user.getStatusValue(), "SUS")) {
            responseJson.put(CommentResult.ATTRIBUTE_NAME, CommentResult.NOT_SIGNED.name().toLowerCase());
            return responseJson.toString();
        }

        commentLike.setUserEmail(user.getEmail());
        commentLike.setArticleIndex(id);
        commentLike.setCommentIndex(commentId);
        IResult result = this.accompanyService.addCommentLike(user, id, commentId);

        //매퍼 마저 작성.
        responseJson.put(CommonResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {

        }
        return responseJson.toString();
    }

    @RequestMapping(value = "like/{id}/{commentId}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteLikeComment(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                    @PathVariable(value = "id") Integer id,
                                    @PathVariable(value = "commentId") Integer commentId,
                                    CommentLikeEntity commentLike
    ) {
        JSONObject responseJson = new JSONObject();
        ArticleEntity article = this.accompanyService.getArticle(id);
        CommentEntity comment = this.accompanyService.getCommentIndex(commentId);
        // 세션에 좋아요 정보 저장
        if (user == null || Objects.equals(user.getStatusValue(), "SUS")) {
            responseJson.put(CommentResult.ATTRIBUTE_NAME, CommentResult.NOT_SIGNED.name().toLowerCase());
            return responseJson.toString();
        }
        IResult result = this.accompanyService.deleteCommentLike(user, id, commentId);
        //매퍼 마저 작성.
        responseJson.put(CommonResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }


    @RequestMapping(value = "modify/{id}", method = RequestMethod.GET)
    public ModelAndView getModify(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @PathVariable(value = "id") int id,
            ModelAndView modelAndView
    ) {
        if (user == null || Objects.equals(user.getStatusValue(), "SUS")) {
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
        if (user == null || Objects.equals(user.getStatusValue(), "SUS") && !user.getEmail().equals(article.getUserEmail())) {
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
        if (user == null || Objects.equals(user.getStatusValue(), "SUS")) {
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

    @RequestMapping(value = "request/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteRequest(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
            @PathVariable(value = "id") int id
    ) throws RollbackException {
        JSONObject responseJson = new JSONObject();
        if (user == null || Objects.equals(user.getStatusValue(), "SUS")) {
            responseJson.put(IResult.ATTRIBUTE_NAME, false);
        }
        IResult result = this.accompanyService.deleteRequest(user, id);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }
}