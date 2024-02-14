package com.dripsoda.community.controllers;

import com.dripsoda.community.dtos.member.ChatSendUserContactDto;
import com.dripsoda.community.entities.accompany.ArticleEntity;
import com.dripsoda.community.entities.accompany.CommentEntity;
import com.dripsoda.community.entities.accompany.ImageEntity;
import com.dripsoda.community.entities.member.*;
import com.dripsoda.community.entities.qna.CategoryEntity;
import com.dripsoda.community.entities.qna.QnaArticleEntity;
import com.dripsoda.community.enums.CommonResult;
import com.dripsoda.community.exceptions.RollbackException;
import com.dripsoda.community.interfaces.IResult;
import com.dripsoda.community.services.AccompanyService;
import com.dripsoda.community.services.MemberService;
import com.dripsoda.community.services.QnaService;
import com.dripsoda.community.utils.CryptoUtils;
import com.dripsoda.community.utils.FileUtil;
import com.dripsoda.community.vos.member.LoginVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller(value = "com.dripsoda.community.controllers.MemberController")
@RequestMapping(value = "/member")
public class MemberController {
    private final MemberService memberService;
    private final QnaService qnaService;
    private final AccompanyService accompanyService;

    @Autowired
    public MemberController(MemberService memberService, QnaService qnaService, AccompanyService accompanyService) {
        this.memberService = memberService;
        this.qnaService = qnaService;
        this.accompanyService = accompanyService;
    }


    @RequestMapping(value = "userEmailCheck", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getUserEmailCheck(UserEntity user) {
        IResult result = this.memberService.checkUserEmail(user);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userRecoverEmail", method = RequestMethod.GET)
    public ModelAndView getUserRecoverEmail(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                            ModelAndView modelAndView) {
        if (user != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.addObject(ContactCountryEntity.ATTRIBUTE_NAME_PLURAL, this.memberService.getContactCountries());
        modelAndView.setViewName("member/userRecoverEmail");
        return modelAndView;
    }

    @RequestMapping(value = "userRecoverEmail", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRecoverEmail(ContactAuthEntity contactAuth) {
        contactAuth.setIndex(-1)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        UserEntity user = UserEntity.build();
        IResult result = this.memberService.findUserEmail(contactAuth, user);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("email", user.getEmail());
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "userRecoverEmailAuth", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getUserRecoverEmailAuth(UserEntity user) throws
            IOException,
            InvalidKeyException,
            NoSuchAlgorithmException {
        user.setEmail(null)
                .setPassword(null)
                .setPolicyTermsAt(null)
                .setPolicyPrivacyAt(null)
                .setPolicyMarketingAt(null)
                .setStatusValue(null)
                .setRegisteredAt(null)
                .setAdmin(false);
        IResult result;
        ContactAuthEntity contactAuth = ContactAuthEntity.build();
        try {
            result = this.memberService.recoverUserEmailAuth(user, contactAuth);
        } catch (RollbackException ex) {
            result = CommonResult.FAILURE;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("salt", contactAuth.getSalt());
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "userRecoverEmailAuth", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRecoverEmailAuth(ContactAuthEntity contactAuth) {
        return this.postUserRegisterAuth(contactAuth); // TODO : Contact authentication of register and email recovery is merged.
    }

    @RequestMapping(value = "userRecoverPassword", method = RequestMethod.GET)
    public ModelAndView getUserRecoverPassword(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                               ModelAndView modelAndView) {
        if (user != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.setViewName("member/userRecoverPassword");
        return modelAndView;
    }

    @RequestMapping(value = "userRecoverPassword", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRecoverPassword(UserEntity user) throws
            MessagingException,
            RollbackException {
        user.setPassword(null)
                .setName(null)
                .setContactCountryValue(null)
                .setContact(null)
                .setPolicyTermsAt(null)
                .setPolicyPrivacyAt(null)
                .setPolicyMarketingAt(null)
                .setStatusValue(null)
                .setRegisteredAt(null)
                .setAdmin(false);
        IResult result = this.memberService.recoverUserPassword(user);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userResetPassword", method = RequestMethod.GET)
    public ModelAndView getUserResetPassword(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                             ModelAndView modelAndView) {
        if (user != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.setViewName("member/userResetPassword");
        return modelAndView;
    }

    @RequestMapping(value = "userResetPassword", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserResetPassword(EmailAuthEntity emailAuth,
                                        UserEntity user) {
        emailAuth.setEmail(null)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        user.setEmail(null)
                .setName(null)
                .setContactCountryValue(null)
                .setContact(null)
                .setPolicyTermsAt(null)
                .setPolicyPrivacyAt(null)
                .setPolicyMarketingAt(null)
                .setStatusValue(null)
                .setRegisteredAt(null)
                .setAdmin(false);
        IResult result;
        try {
            result = this.memberService.resetPassword(emailAuth, user);
        } catch (RollbackException ex) {
            result = ex.result;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userLogin", method = RequestMethod.GET)
    public ModelAndView getUserLogin(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                     ModelAndView modelAndView) {
        if (user != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.setViewName("member/userLogin");
        return modelAndView;
    }

    @RequestMapping(value = "userLogin", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserLogin(@RequestParam(value = "autosign", required = false) Optional<Boolean> autosignOptional,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                HttpSession session,
                                UserEntity user,
                                LoginVo loginVo) {
        boolean autosign = autosignOptional.orElse(false);
        user.setName(null)
                .setContactCountryValue(null)
                .setContact(null)
                .setPolicyTermsAt(null)
                .setPolicyPrivacyAt(null)
                .setPolicyMarketingAt(null)
                .setStatusValue(null)
                .setRegisteredAt(null)
                .setAdmin(false);
        IResult result = this.memberService.loginUser(user);

        if (result == CommonResult.SUCCESS) {
            session.setAttribute(UserEntity.ATTRIBUTE_NAME, user);
            if (autosign) {
                Cookie autoLoginCookie = new Cookie("autoLoginCookie", session.getId());
                autoLoginCookie.setPath("/");
                long limitTime = 60 * 60 * 24 * 90;
                autoLoginCookie.setMaxAge((int) limitTime);
                response.addCookie(autoLoginCookie);
            }
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userRegister", method = RequestMethod.GET)
    public ModelAndView getUserRegister(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                        ModelAndView modelAndView) {
        if (user != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.addObject(ContactCountryEntity.ATTRIBUTE_NAME_PLURAL, this.memberService.getContactCountries());
        modelAndView.setViewName("member/userRegister");
        return modelAndView;
    }

    @RequestMapping(value = "userRegister", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRegister(@RequestParam(value = "policyMarketing", required = true) boolean policyMarketing,
                                   ContactAuthEntity contactAuth,
                                   UserEntity user) throws IOException {
        //프로필은 없으니 기본값 설정.
        String resourcePath = "static/resources/images/vector_profile_default.svg";
        byte[] readyByte = FileUtil.getBytes(resourcePath);

        contactAuth.setIndex(-1)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        user.setPolicyTermsAt(new Date())
                .setPolicyPrivacyAt(new Date())
                .setPolicyMarketingAt(policyMarketing ? new Date() : null)
                .setStatusValue("OKY")
                .setRegisteredAt(new Date())
                .setAdmin(false)
                .setProfileId("no")
                .setProfileData(readyByte);
        IResult result;
        try {
            result = this.memberService.createUser(contactAuth, user);

        } catch (RollbackException ex) {
            result = ex.result;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userRegisterDone", method = RequestMethod.GET)
    public ModelAndView getUserRegisterDone(ModelAndView modelAndView) {
        modelAndView.setViewName("member/userRegisterDone");
        return modelAndView;
    }

    @RequestMapping(value = "userRegisterAuth", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getUserRegisterAuth(ContactAuthEntity contactAuth) throws
            InvalidKeyException,
            IOException,
            NoSuchAlgorithmException {
        contactAuth.setIndex(-1)
                .setCode(null)
                .setSalt(null)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        IResult result;
        try {
            result = this.memberService.registerAuth(contactAuth);
        } catch (RollbackException ex) {
            result = ex.result;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("salt", contactAuth.getSalt());
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "userRegisterAuth", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRegisterAuth(ContactAuthEntity contactAuth) {
        contactAuth.setIndex(-1)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        IResult result;
        try {
            result = this.memberService.checkContactAuth(contactAuth);
        } catch (RollbackException ex) {
            result = ex.result;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userLogout", method = RequestMethod.GET)
    public ModelAndView getUserLogout(ModelAndView modelAndView,
                                      HttpSession session) {
        session.removeAttribute(UserEntity.ATTRIBUTE_NAME);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @RequestMapping(value = "manager", method = RequestMethod.GET)
    public ModelAndView getManager(ModelAndView modelAndView,
                                   HttpSession session,
                                   @RequestParam(value = "tab", required = false, defaultValue = "user") String tab
    ) {
        UserEntity user = (UserEntity) session.getAttribute(UserEntity.ATTRIBUTE_NAME);
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        }
        if (!user.isAdmin()) {
            session.invalidate();
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        }
        if (tab == null || tab.equals("user") || (!tab.equals("accompany") && !tab.equals("qna") && !tab.equals("comment"))) {
            modelAndView.addObject(ContactCountryEntity.ATTRIBUTE_NAME_PLURAL, this.memberService.getContactCountries());
        }
        modelAndView.addObject(UserEntity.ATTRIBUTE_NAME_PLURAL, this.memberService.getUsers());
        modelAndView.addObject(ArticleEntity.ATTRIBUTE_NAME_PLURAL, this.accompanyService.getArticlesForManager());
        modelAndView.addObject(ChatEntity.ATTRIBUTE_NAME_PLURAL, this.memberService.getChats());
        modelAndView.addObject(CommentEntity.ATTRIBUTE_NAME_PLURAL,this.accompanyService.getComments());
        modelAndView.setViewName("member/manager");
        return modelAndView;
    }


    @RequestMapping(value = "manager/write", method = RequestMethod.GET)
    public ModelAndView getWrite(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            HttpSession session,
            ModelAndView modelAndView
    ) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        }
        if (!user.isAdmin()) {
            session.invalidate();
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        }
        modelAndView.addObject(CategoryEntity.ATTRIBUTE_NAME_PLURAL, this.qnaService.getCategories());
        modelAndView.setViewName("member/write");
        return modelAndView;
    }


    @RequestMapping(value = "manager/write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWrite(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
            QnaArticleEntity qnaArticle,
            @RequestParam(value = "categoryId") int categoryId
    ) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM--dd");
        qnaArticle.setIndex(-1)
                .setCategoryId(categoryId)
                .setUserEmail(user.getEmail())
                .setCreatedAt(new Date());
        IResult result = this.qnaService.putArticle(qnaArticle);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("id", qnaArticle.getIndex());
        }
        return responseJson.toString();
    }
    //qna게시판은 일반 회원 접근 불가 + coverImage가 없기 때문에 새로 만들어야됨. // 유지 보수 측면에서 동행 게시판과는 다르기 때문에 장점이 될 수 있어서 다른 게시판을 하나 더 생성해야되겠다.
    //qna 게시판의 종류는 - 공지사항, 이벤트, qna 총 3가지 category설정해야됨. 인덱스 값 주고 value 값으로 3가지만 주면 될듯.
// read의 경우 /member/manage/write가 아니라 /qna/read가 되어야 되기 때문 //

    @RequestMapping(value = "manager/read/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteRead(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                             @PathVariable(value = "id") int id) {
        JSONObject responseJson = new JSONObject();
        int categoryId = this.qnaService.getCategoryId(id);
        QnaArticleEntity qnaArticle = this.qnaService.getArticle(id, categoryId);
        if (qnaArticle == null) {
            responseJson.put(IResult.ATTRIBUTE_NAME, CommonResult.FAILURE);
            return responseJson.toString();
        }
        //i라는 이름은 inaccessible 보여지는 이름을 완전히 보여주는건 올바르지 않다고 생각.
        if (user == null || !user.isAdmin() && !user.getEmail().equals(qnaArticle.getUserEmail())) {
            responseJson.put(IResult.ATTRIBUTE_NAME, "i");
            return responseJson.toString();
        }
        IResult result = this.qnaService.deleteQnaArticle(id);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "manager/image/{id}", method = RequestMethod.GET)
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


    @RequestMapping(value = "manager/image", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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
            responseJson.put("url", String.format("http://localhost:8080/member/manager/image/%d", image.getIndex()));
        } else {
            JSONObject errorJson = new JSONObject();
            errorJson.put("message", "이미지 업로드에 실패하였습니다. 잠시 후 다시 시도해주세요.");
            responseJson.put("error", errorJson);
        }
        return responseJson.toString();
    }

    //qna 글 수정
    @RequestMapping(value = "manager/modify/{id}", method = RequestMethod.GET)
    public ModelAndView getModify(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
            @PathVariable(value = "id") int id,
            HttpSession session,
            ModelAndView modelAndView
    ) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        }
        if (!user.isAdmin()) {
            session.invalidate();
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        }
        modelAndView.setViewName("member/modify");
        return modelAndView;
    }

    @RequestMapping(value = "manager/modify/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchModify(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                              @PathVariable(value = "id") int id,
                              HttpServletResponse response) throws JsonProcessingException {
        int categoryId = this.qnaService.getCategoryId(id);
        QnaArticleEntity qnaArticle = this.qnaService.getArticle(id, categoryId);
        if (qnaArticle == null) {
            response.setStatus(404);
            return null;
        }
        if (user == null || !user.isAdmin() && !user.getEmail().equals(qnaArticle.getUserEmail())) {
            response.setStatus(403);
            return null;
        }
        return new ObjectMapper().writeValueAsString(qnaArticle);
    }

    @RequestMapping(value = "manager/modify/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postModify(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
            @PathVariable(value = "id") int id,
            QnaArticleEntity qnaArticle
    ) throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int categoryId = this.qnaService.getCategoryId(id);
        qnaArticle.setIndex(id)
                .setUserEmail(user.getEmail())
                .setCategoryId(categoryId);
        IResult result = this.qnaService.modifyQnaArticle(qnaArticle);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }


    @RequestMapping(value = "userMy", method = RequestMethod.GET)
    public ModelAndView getUserMy(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                  @RequestParam(value = "tab", required = false, defaultValue = "info") String tab,
                                  ModelAndView modelAndView) {
        if (user == null) {
            modelAndView.setViewName("redirect:/member/userLogin");
            return modelAndView;
        }
        if (tab == null || tab.equals("info") || (!tab.equals("trip") && !tab.equals("book") && !tab.equals("comment") && !tab.equals("accompany") && !tab.equals("truncate"))) {
            modelAndView.addObject(ContactCountryEntity.ATTRIBUTE_NAME_PLURAL, this.memberService.getContactCountries());
        }

        //내댓글
        modelAndView.addObject(CommentEntity.ATTRIBUTE_NAME_PLURAL, this.accompanyService.getCommentsByUserEmail(user.getEmail()));
        //내동행게시글
        modelAndView.addObject(ArticleEntity.ATTRIBUTE_NAME_PLURAL, this.accompanyService.getArticleForUserMy(user.getEmail()));
        modelAndView.addObject(UserEntity.ATTRIBUTE_NAME, this.memberService.getUser(user.getEmail()));

        modelAndView.setViewName("member/userMy");
        return modelAndView;
    }

    @RequestMapping(value = "userMyInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postUserMyInfo(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity currentUser,
                                 @RequestParam(value = "oldPassword") String oldPassword,
                                 @RequestParam(value = "changePassword") Optional<Boolean> changePasswordOptional,
                                 @RequestParam(value = "changeContact") Optional<Boolean> changeContactOptional,
                                 @RequestParam(value = "newPassword", required = false, defaultValue = "") String newPassword,
                                 @RequestParam(value = "newContact", required = false, defaultValue = "") String newContact,
                                 @RequestParam(value = "newContactAuthCode", required = false, defaultValue = "") String newContactAuthCode,
                                 @RequestParam(value = "newContactAuthSalt", required = false, defaultValue = "") String newContactAuthSalt) throws MessagingException, IOException {
        UserEntity newUser = UserEntity.build();
        if (changePasswordOptional.orElse(false)) {
            newUser.setPassword(newPassword);
        }
        if (changeContactOptional.orElse(false)) {
            newUser.setContact(newContact);
        }
        ContactAuthEntity contactAuth = ContactAuthEntity.build()
                .setContact(newContact)
                .setCode(newContactAuthCode)
                .setSalt(newContactAuthSalt);
        IResult result;
        try {
            result = this.memberService.modifyUser(currentUser, newUser, oldPassword, contactAuth);
        } catch (RollbackException ex) {
            result = ex.result;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }


    @RequestMapping(value = "userMyInfoProfileImage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postUserMyInfoProfileImage(@RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                                             @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user) throws IOException {

        String resourcePath = "static/resources/images/vector_profile_default.svg";
        byte[] readyByte = FileUtil.getBytes(resourcePath);

        if (profileImage == null) {
            user.setProfileData(readyByte);
            user.setProfileId("no");
        } else {
            String profileId = String.format("%s%f%f", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()),
                    Math.random(),
                    Math.random());
            profileId = CryptoUtils.hashSha512(profileId);
            user.setProfileData(profileImage.getBytes());
            user.setProfileId(profileId);
        }
        IResult result = this.memberService.modifyUserProfileImage(user);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userMyInfoNickname", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postUserMyInfoNickname(@RequestParam(value = "profileNickname", required = false) String profileNickname,
                                         @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity currentUser) {

        UserEntity newUser = UserEntity.build();
        if (profileNickname != null) {
            newUser.setNickname(profileNickname);
        }
        JSONObject responseJson = new JSONObject();
        IResult result;
        try {
            result = this.memberService.modifyUserProfileNickname(currentUser,newUser);
        } catch (RollbackException ex) {
            result = ex.result;
        }
        System.out.println(result);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }


    @RequestMapping(value = "userMyInfoAuth", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getUserMyInfoAuth(@RequestParam(value = "newContact") String newContact) throws
            InvalidKeyException,
            IOException,
            NoSuchAlgorithmException {
        ContactAuthEntity contactAuth = ContactAuthEntity.build().setContact(newContact);
        IResult result;
        try {
            result = this.memberService.modifyUserContactAuth(contactAuth);
        } catch (RollbackException ex) {
            result = ex.result;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("salt", contactAuth.getSalt());
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "userMyInfoAuth", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserMyInfoAuth(@RequestParam(value = "newContact") String newContact,
                                     @RequestParam(value = "newContactAuthCode") String newContactAuthCode,
                                     @RequestParam(value = "newContactAuthSalt") String newContactAuthSalt) throws
            RollbackException {
        ContactAuthEntity contactAuth = ContactAuthEntity.build()
                .setContact(newContact)
                .setCode(newContactAuthCode)
                .setSalt(newContactAuthSalt);
        IResult result = this.memberService.checkContactAuth(contactAuth);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "profile-id", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getProfileId(@RequestParam(value = "profileId", required = true) String profileId,
                                               HttpServletResponse response) {
        UserEntity profileUser = this.memberService.getProfileImage(profileId);


        if (profileUser.getProfileId() == null) {
            response.setStatus(404);
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.OK;
        headers.add("Content-Length", String.valueOf(profileUser.getProfileData().length));
        headers.add("Content-Type", "image/png");
        return new ResponseEntity<>(profileUser.getProfileData(), headers, status);
    }


    @RequestMapping(value = "userMyInfoDelete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteUserMyInfoDelete(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                         @RequestParam(value = "findAccompany", required = false) Optional<Boolean> findAccompanyOptional,
                                         @RequestParam(value = "tripEnds", required = false) Optional<Boolean> tripEndsOptional,
                                         @RequestParam(value = "travelProducts", required = false) Optional<Boolean> travelProductsOptional,
                                         @RequestParam(value = "badManners", required = false) Optional<Boolean> badMannersOptional,
                                         @RequestParam(value = "inconvenience", required = false) Optional<Boolean> inconvenienceOptional,
                                         @RequestParam(value = "new", required = false) Optional<Boolean> newOptional,
                                         @RequestParam(value = "useful", required = false) Optional<Boolean> usefulOptional,
                                         @RequestParam(value = "content") String content,
                                         HttpSession session) {

        JSONObject responseJson = new JSONObject();
        FeedbackEntity feedback = FeedbackEntity.build()
                .setUserEmail(user.getEmail())
                .setCreatedAt(new Date())
                .setContent(content);

        feedback.setFind(findAccompanyOptional.orElse(false));
        feedback.setTrip(tripEndsOptional.orElse(false));
        feedback.setProduct(travelProductsOptional.orElse(false));
        feedback.setManner(badMannersOptional.orElse(false));
        feedback.setConvenience(inconvenienceOptional.orElse(false));
        feedback.setNew(newOptional.orElse(false));
        feedback.setUseful(usefulOptional.orElse(false));
        this.memberService.createFeedback(feedback);
        IResult result;
        try {
            result = this.memberService.deleteUser(user);
        } catch (RollbackException ex) {
            result = ex.result;
        }
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        session.removeAttribute(UserEntity.ATTRIBUTE_NAME);
        return responseJson.toString();
    }


    @RequestMapping(value = "chatMessage/{rid}", method = RequestMethod.GET)
    @ResponseBody
    public String getChatMessage(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                 @PathVariable(value = "rid", required = false) int rid) {
        JSONObject responseJson = new JSONObject();
        if (user == null) {
            responseJson.put(IResult.ATTRIBUTE_NAME, false);
        }
        if (!(rid == 1 || rid == 2)) {
            responseJson.put(IResult.ATTRIBUTE_NAME, false);
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "chatMessage/{rid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postChatMessage(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
            @RequestParam(value = "rid", required = false) int rid,
            @RequestParam(value = "content") String content
    ) throws NoSuchAlgorithmException, IOException, InvalidKeyException, RollbackException {
        JSONObject responseJson = new JSONObject();
        IResult result = this.memberService.putChat(user, rid, content);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("rid", rid);
        }
        return responseJson.toString();
    }

    //관리자가 채팅 보냄 - 보낸 사람의 인덱스 번호로  답변하기 -
    @RequestMapping(value = "managerMessage/{id}", method = RequestMethod.GET)
    public ModelAndView getManagerMessage(
            HttpServletResponse response,
            HttpSession session,
            @PathVariable(value = "id") int id,
            ModelAndView modelAndView
    ) {
        UserEntity user = (UserEntity) session.getAttribute(UserEntity.ATTRIBUTE_NAME);
        ChatEntity chat = this.memberService.getChatbyIndex(id);
        if (chat == null) {
            response.setStatus(404);
            modelAndView.setViewName("error/error"); // 커스텀 에러 페이지로 이동하도록 설정
            return modelAndView;
        }
        if (user == null || !user.isAdmin()) {
            // 유저가 로그인하지 않았거나, 관리자 권한이 없는 경우 404 Forbidden 응답 반환
            response.setStatus(404);
            modelAndView.setViewName("error/error"); // 커스텀 에러 페이지로 이동하도록 설정
            return modelAndView;
        }
        modelAndView.setViewName("member/managerChat");
        return modelAndView;
    }

    @RequestMapping(value = "managerMessage/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postManagerMessage(
            @SessionAttribute(value = UserEntity.ATTRIBUTE_NAME) UserEntity user,
            @PathVariable(value = "id") int id,
            @RequestParam(value = "content") String content
    ) throws NoSuchAlgorithmException, IOException, InvalidKeyException, RollbackException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM--dd");
        ChatSendUserContactDto chat = this.memberService.getChatbyIndex(id);
        IResult result = this.memberService.putMnagerChat(chat, user, content);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
//            responseJson.put("id", qnaArticle.getIndex());
        }
        return responseJson.toString();
    }

}












