package com.dripsoda.community.services;

import com.dripsoda.community.components.MailComponent;
import com.dripsoda.community.components.SmsComponent;
import com.dripsoda.community.dtos.member.ChatSendUserContactDto;
import com.dripsoda.community.entities.member.*;
import com.dripsoda.community.enums.CommonResult;
import com.dripsoda.community.enums.member.ChatResult;
import com.dripsoda.community.enums.member.UserLoginResult;
import com.dripsoda.community.exceptions.RollbackException;
import com.dripsoda.community.interfaces.IResult;
import com.dripsoda.community.mappers.IMemberMapper;
import com.dripsoda.community.regex.MemberRegex;
import com.dripsoda.community.utils.CryptoUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Service(value = "com.dripsoda.community.services.MemberService")
public class MemberService {
    private final IMemberMapper memberMapper;
    private final SmsComponent smsComponent;
    private final MailComponent mailComponent;

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    public MemberService(IMemberMapper memberMapper, SmsComponent smsComponent, MailComponent mailComponent) {
        this.memberMapper = memberMapper;
        this.smsComponent = smsComponent;
        this.mailComponent = mailComponent;
    }

    @Transactional
    protected IResult createContactAuth(ContactAuthEntity contactAuth) throws
            InvalidKeyException,
            IOException,
            NoSuchAlgorithmException,
            RollbackException {
        if (contactAuth.getContact() == null || !contactAuth.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
        }
        Date createdAt = new Date();
        Date expiresAt = DateUtils.addMinutes(createdAt, 5);
        String code = RandomStringUtils.randomNumeric(6);
        String salt = CryptoUtils.hashSha512(String.format("%s%s%d%f%f",
                contactAuth.getContact(),
                code,
                createdAt.getTime(),
                Math.random(),
                Math.random()));
        contactAuth.setCode(code)
                .setSalt(salt)
                .setCreatedAt(createdAt)
                .setExpiresAt(expiresAt)
                .setExpired(false);
        if (this.memberMapper.insertContactAuth(contactAuth) == 0) {
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            // return CommonResult.FAILURE;
            throw new RollbackException();
        }
        String smsContent = String.format("[드립소다] 인증번호 [%s]를 입력해 주세요.", contactAuth.getCode());
        if (this.smsComponent.send(contactAuth.getContact(), smsContent) != 202) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult checkContactAuth(ContactAuthEntity contactAuth) throws
            RollbackException {
        if (contactAuth.getContact() == null ||
                contactAuth.getCode() == null ||
                contactAuth.getSalt() == null ||
                !contactAuth.getContact().matches(MemberRegex.USER_CONTACT) ||
                !contactAuth.getCode().matches(MemberRegex.CONTACT_AUTH_CODE) ||
                !contactAuth.getSalt().matches(MemberRegex.CONTACT_AUTH_SALT)) {
            return CommonResult.FAILURE;
        }
        contactAuth = this.memberMapper.selectContactAuthByContactCodeSalt(contactAuth);
        if (contactAuth == null) {
            return CommonResult.FAILURE;
        }
        if (contactAuth.isExpired() || new Date().compareTo(contactAuth.getExpiresAt()) > 0) {
            return CommonResult.FAILURE_EXPIRED;
        }
        contactAuth.setExpired(true);
        if (this.memberMapper.updateContactAuth(contactAuth) == 0) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

    @Transactional
    public CommonResult createUser(ContactAuthEntity contactAuth, UserEntity user) throws
            RollbackException {
        if (contactAuth.getContact() == null ||
                contactAuth.getCode() == null ||
                contactAuth.getSalt() == null ||
                !contactAuth.getContact().matches(MemberRegex.USER_CONTACT) ||
                !contactAuth.getCode().matches(MemberRegex.CONTACT_AUTH_CODE) ||
                !contactAuth.getSalt().matches(MemberRegex.CONTACT_AUTH_SALT)) {
            return CommonResult.FAILURE;
        }
        contactAuth = this.memberMapper.selectContactAuthByContactCodeSalt(contactAuth);
        if (contactAuth == null || !contactAuth.isExpired()) {
            return CommonResult.FAILURE;
        }
        if (user.getEmail() == null ||
                user.getPassword() == null ||
                user.getName() == null ||
                user.getContact() == null ||
                !user.getEmail().matches(MemberRegex.USER_EMAIL) ||
                !user.getPassword().matches(MemberRegex.USER_PASSWORD) ||
                !user.getName().matches(MemberRegex.USER_NAME) ||
                !user.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
        }
        //닉네임 기본값 스플릿
        user.setNickname(user.getEmail().split("@")[0]);
        //프로필 기본 설정 없으니 유효성 검증뒤 no 값
        user.setProfileId("no");
        user.setPassword(CryptoUtils.hashSha512(user.getPassword()));
        if (this.memberMapper.insertUser(user) == 0) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

    public IResult checkUserEmail(UserEntity user) {
        if (user.getEmail() == null || !user.getEmail().matches(MemberRegex.USER_EMAIL)) {
            return CommonResult.FAILURE;
        }
        user = this.memberMapper.selectUserByEmail(user);
        return user == null
                ? CommonResult.SUCCESS
                : CommonResult.DUPLICATE;
    }

    public ContactCountryEntity[] getContactCountries() {
        return this.memberMapper.selectContactCountries();
    }

    @Transactional
    public IResult recoverUserEmailAuth(UserEntity user, ContactAuthEntity contactAuth) throws
            IOException,
            InvalidKeyException,
            NoSuchAlgorithmException,
            RollbackException {
        if (user.getName() == null ||
                user.getContact() == null ||
                !user.getName().matches(MemberRegex.USER_NAME) ||
                !user.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
        }
        user = this.memberMapper.selectUserByNameContact(user);
        if (user == null) {
            return CommonResult.FAILURE;
        }
        contactAuth.setContact(user.getContact());
        if (this.createContactAuth(contactAuth) != CommonResult.SUCCESS) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult registerAuth(ContactAuthEntity contactAuth) throws
            IOException,
            InvalidKeyException,
            NoSuchAlgorithmException,
            RollbackException {
        if (contactAuth.getContact() == null || !contactAuth.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
        }
        if (this.memberMapper.selectUserByContact(UserEntity.build().setContact(contactAuth.getContact())) != null) {
            return CommonResult.DUPLICATE;
        }
        if (this.createContactAuth(contactAuth) != CommonResult.SUCCESS) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult findUserEmail(ContactAuthEntity contactAuth, UserEntity user) {
        if (contactAuth.getContact() == null ||
                contactAuth.getCode() == null ||
                contactAuth.getSalt() == null ||
                !contactAuth.getContact().matches(MemberRegex.USER_CONTACT) ||
                !contactAuth.getCode().matches(MemberRegex.CONTACT_AUTH_CODE) ||
                !contactAuth.getSalt().matches(MemberRegex.CONTACT_AUTH_SALT)) {
            return CommonResult.FAILURE;
        }
        contactAuth = this.memberMapper.selectContactAuthByContactCodeSalt(contactAuth);
        if (contactAuth == null || !contactAuth.isExpired()) {
            return CommonResult.FAILURE;
        }
        UserEntity foundUser = this.memberMapper.selectUserByContact(user.setContact(contactAuth.getContact()));
        if (foundUser == null) {
            return CommonResult.FAILURE;
        }
        user.setEmail(foundUser.getEmail());
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult recoverUserPassword(UserEntity user) throws
            MessagingException,
            RollbackException {
        if (user.getEmail() == null || !user.getEmail().matches(MemberRegex.USER_EMAIL)) {
            return CommonResult.FAILURE;
        }
        user = this.memberMapper.selectUserByEmail(user);
        if (user == null) {
            return CommonResult.FAILURE;
        }
        Date createdAt = new Date();
        String code = CryptoUtils.hashSha512(String.format("%s%d%f%f",
                user.getEmail(),
                createdAt.getTime(),
                Math.random(),
                Math.random()));
        EmailAuthEntity emailAuth = EmailAuthEntity.build()
                .setEmail(user.getEmail())
                .setCode(code)
                .setCreatedAt(createdAt)
                .setExpiresAt(DateUtils.addMinutes(createdAt, 10));
        if (this.memberMapper.insertEmailAuth(emailAuth) == 0) {
            throw new RollbackException();
        }
        final String from = "fkgiwos4@gmail.com";
        final String subject = "[드립소다] 비밀번호 재설정";
        final String viewName = "member/userRecoverPasswordMail";
        Context context = new Context();
        context.setVariable("name", user.getName());
        context.setVariable("index", emailAuth.getIndex());
        context.setVariable("code", emailAuth.getCode());
        this.mailComponent.sendHtml(from, emailAuth.getEmail(), subject, viewName, context);
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult resetPassword(EmailAuthEntity emailAuth, UserEntity user) throws
            RollbackException {
        if (emailAuth.getIndex() < 1 ||
                emailAuth.getCode() == null ||
                user.getPassword() == null ||
                !emailAuth.getCode().matches(MemberRegex.EMAIL_AUTH_CODE) ||
                !user.getPassword().matches(MemberRegex.USER_PASSWORD)) {
            return CommonResult.FAILURE;
        }
        EmailAuthEntity existingEmailAuth = this.memberMapper.selectEmailAuthByIndex(emailAuth);
        if (existingEmailAuth == null || !existingEmailAuth.getCode().equals(emailAuth.getCode())) {
            return CommonResult.FAILURE;
        }
        if (existingEmailAuth.isExpired() || new Date().compareTo(existingEmailAuth.getExpiresAt()) > 0) {
            return CommonResult.FAILURE_EXPIRED;
        }
        existingEmailAuth.setExpired(true);
        if (this.memberMapper.updateEmailAuth(existingEmailAuth) == 0) {
            throw new RollbackException();
        }

        user.setEmail(existingEmailAuth.getEmail());
        UserEntity existingUser = this.memberMapper.selectUserByEmail(user);
        if (existingUser == null) {
            return CommonResult.FAILURE;
        }
        existingUser.setPassword(CryptoUtils.hashSha512(user.getPassword()));
        if (this.memberMapper.updateUser(existingUser) == 0) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult loginUser(UserEntity user) {
        if (user.getEmail() == null ||
                user.getPassword() == null ||
                !user.getEmail().matches(MemberRegex.USER_EMAIL) ||
                !user.getPassword().matches(MemberRegex.USER_PASSWORD)) {
            return CommonResult.FAILURE;
        }
        user.setPassword(CryptoUtils.hashSha512(user.getPassword()));
        UserEntity existingUser = this.memberMapper.selectUserByEmailPassword(user);
        if (existingUser == null) {
            return CommonResult.FAILURE;
        }
        user.setEmail(existingUser.getEmail())
                .setPassword(existingUser.getPassword())
                .setName(existingUser.getName())
                .setNickname(existingUser.getNickname())
                .setContactCountryValue(existingUser.getContactCountryValue())
                .setContact(existingUser.getContact())
                .setPolicyTermsAt(existingUser.getPolicyTermsAt())
                .setPolicyPrivacyAt(existingUser.getPolicyPrivacyAt())
                .setPolicyMarketingAt(existingUser.getPolicyMarketingAt())
                .setStatusValue(existingUser.getStatusValue())
                .setRegisteredAt(existingUser.getRegisteredAt())
                .setAdmin(existingUser.isAdmin())
                .setProfileData(existingUser.getProfileData())
                .setProfileId(existingUser.getProfileId());
        if (user.getStatusValue().equals("SUS")) {
            return UserLoginResult.SUSPENDED;
        }
        return CommonResult.SUCCESS;
    }

    public UserEntity getProfileImage(String profileId) {
        return this.memberMapper.selectProfileImage(profileId);
    }

    @Transactional
    public IResult modifyUser(UserEntity currentUser, UserEntity newUser, String oldPassword, ContactAuthEntity contactAuth) throws
            RollbackException {
        if (currentUser == null || oldPassword == null ||
                currentUser.getPassword() == null ||
                !CryptoUtils.hashSha512(oldPassword).equals(currentUser.getPassword())) {
            return CommonResult.FAILURE; // 현재 비밀번호 틀림
        }
        if (newUser.getPassword() != null && !newUser.getPassword().matches(MemberRegex.USER_PASSWORD)) {
            return CommonResult.FAILURE; // 신규 비밀번호 정규화 실패
        }

        if (newUser.getContact() != null && (!newUser.getContact().matches(MemberRegex.USER_CONTACT) || this.checkContactAuth(contactAuth) != CommonResult.FAILURE_EXPIRED)) {
            return CommonResult.FAILURE; // 신규 연락처 정규화 실패 혹은 인증 실패
        }
        String oldCurrentPassword = currentUser.getPassword(); // Update 실패시 세션에 있는 객체가 가진 원래 값으로 되돌려 놓기 위해 백업 해놓아야함.
        String oldCurrentContact = currentUser.getContact();


        if (newUser.getPassword() != null) {
            currentUser.setPassword(CryptoUtils.hashSha512(newUser.getPassword()));
        }
        if (newUser.getContact() != null) {
            currentUser.setContact(newUser.getContact());
        }

        //0이랑 비교
        int record = this.memberMapper.updateUser(currentUser);
        if (record == 0) {
            currentUser.setPassword(oldCurrentPassword);
            currentUser.setContact(oldCurrentContact);
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

    public IResult modifyUserContactAuth(ContactAuthEntity contactAuth) throws
            InvalidKeyException,
            IOException,
            NoSuchAlgorithmException,
            RollbackException {
        if (contactAuth.getContact() == null ||
                !contactAuth.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
        }
        if (this.memberMapper.selectUserByContact(UserEntity.build().setContact(contactAuth.getContact())) != null) {
            return CommonResult.DUPLICATE;
        }
        return this.createContactAuth(contactAuth);
    }

    public IResult modifyUserProfileNickname(UserEntity currentUser, UserEntity newUser) throws RollbackException {
        if (currentUser == null || currentUser.getNickname() == null) {
            return CommonResult.FAILURE;
        }
        String oldCurrentNickname = currentUser.getNickname();

        if (newUser.getNickname() != null) {
            currentUser.setNickname(newUser.getNickname());
        }

        int record = this.memberMapper.updateUser(currentUser);
        if (record == 0) {
            currentUser.setNickname(oldCurrentNickname);
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

    public IResult modifyUserProfileImage(UserEntity user) {
        if (user.getProfileId() == null || user.getProfileData() == null) {
            return CommonResult.FAILURE;
        }
        return this.memberMapper.updateUser(user) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;

    }

    @Transactional
    public IResult putChat(UserEntity user, int roomIndex, String content) throws NoSuchAlgorithmException, IOException, InvalidKeyException, RollbackException {
        if (user == null) {
            return ChatResult.NOT_SIGNED;
        }
        if (!(roomIndex == 1 || roomIndex == 2)) {
            return ChatResult.NOT_FOUND;
        }
        // 나머지 로직
        //관리자 아이디 찾기
        UserEntity admin = this.memberMapper.selectUserByAdmin();
        //readTime 의 경우 일단 현재 만들어진 시간 후에 update로 변경.
        ChatEntity chat = ChatEntity.build()
                .setSendUserEmail(user.getEmail())
                .setReceiveUserEmail(!user.getEmail().equals(admin.getEmail()) ? admin.getEmail() : user.getEmail())
                .setRoom(roomIndex)
                .setSendTime(new Date())
                .setReadTime(new Date())
                .setReadChecked(0)
                .setContent(content);

        int insertChat = this.memberMapper.insertChat(chat);

        if (insertChat > 0) {
            String smsContent;
            smsContent = "[드립소다] 고객님의 답변이 등록되었습니다.";
            if (this.smsComponent.send(user.getContact(), smsContent) != 202) {
                throw new RollbackException();
            }
            return CommonResult.SUCCESS;
        } else {
            return CommonResult.FAILURE;
        }
    }

    @Transactional
    public IResult putMnagerChat(ChatSendUserContactDto chat, UserEntity user, String content) throws NoSuchAlgorithmException, IOException, InvalidKeyException, RollbackException {
        if (chat == null) {
            return ChatResult.NOT_FOUND;
        }
        if (user == null) {
            return ChatResult.NOT_SIGNED;
        }
        //readTime 의 경우 일단 현재 만들어진 시간 후에 update로 변경.
        ChatEntity mangerChat = ChatEntity.build()
                .setSendUserEmail(user.getEmail())
                .setReceiveUserEmail(chat.getSendUserEmail())
                .setRoom(1)
                .setSendTime(new Date())
                .setReadTime(new Date())
                .setReadChecked(0)
                .setContent(content);

        chat.setReadTime(new Date());
        chat.setReadChecked(1);
        int insertChat = this.memberMapper.insertChat(mangerChat);
        if (insertChat > 0) {
            int updateChat = this.memberMapper.updateChat(chat);
            if (updateChat > 0) {
                //chat유저의 contact
                String smsContent;
                smsContent = "[드립소다] 고객님이 등록해주신 질문에 대한 답변이 완료 되었습니다.";
                if (this.smsComponent.send(chat.getSendUserContact(), smsContent) != 202) {
                    throw new RollbackException();
                }
                return CommonResult.SUCCESS;
            } else {
                return CommonResult.FAILURE;
            }
        } else {
            return CommonResult.FAILURE;
        }
    }

    public IResult createFeedback(FeedbackEntity feedback) {
        return this.memberMapper.insertFeedback(feedback) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public IResult deleteUser(UserEntity user) throws RollbackException {
        return this.memberMapper.deleteUser(user) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public List<ChatEntity> chatAdminCheckList(String email) {
        return this.memberMapper.selectAdminByChat(email);
    }

    public List<ChatEntity> chatUserCheckList(String email) {
        return this.memberMapper.selectUserByChat(email);
    }

    public List<ChatEntity> getChatsAll(String email) {
        return this.memberMapper.selectUserByChats(email);
    }

    public ChatSendUserContactDto getChatbyIndex(int id) {
        return this.memberMapper.selectChatByIndex(id);
    }

    public List<ChatEntity> getChats() {
        return this.memberMapper.selectChats();
    }

    public UserEntity getUser(String email) {
        return this.memberMapper.selectUserByEmail(UserEntity.build().setEmail(email));
    }

    public List<UserEntity> getUsers() {
        return this.memberMapper.selectUsersForHome();
    }
}















