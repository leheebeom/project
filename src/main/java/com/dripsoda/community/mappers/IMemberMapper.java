package com.dripsoda.community.mappers;

import com.dripsoda.community.dtos.member.ChatSendUserContactDto;
import com.dripsoda.community.entities.member.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IMemberMapper {
    int deleteUser(UserEntity user);

    int insertContactAuth(ContactAuthEntity contactAuth);

    int insertEmailAuth(EmailAuthEntity emailAuth);

    int insertUser(UserEntity user);

    int insertChat(ChatEntity chat);

    int insertFeedback(FeedbackEntity feedback);

    int updateContactAuth(ContactAuthEntity contactAuth);

    int updateEmailAuth(EmailAuthEntity emailAuth);

    int updateUser(UserEntity user);

    int updateChat(ChatEntity chat);

    ContactCountryEntity[] selectContactCountries();

    List<UserEntity> selectUsersForHome();

    List<ChatEntity> selectUserByChat(@Param(value = "email") String email);

    List<ChatEntity> selectUserByChats(@Param(value = "email") String email);

    List<ChatEntity> selectAdminByChat(@Param(value = "email") String email);

    List<ChatEntity> selectChats();

    ContactAuthEntity selectContactAuthByContactCodeSalt(ContactAuthEntity contactAuth);

    EmailAuthEntity selectEmailAuthByIndex(EmailAuthEntity emailAuth);

    UserEntity selectUserByEmail(UserEntity user);

    UserEntity selectUserByPassword(UserEntity user);

    UserEntity selectUserByEmailPassword(UserEntity user);

    UserEntity selectUserByNameContact(UserEntity user);

    UserEntity selectProfileImage(@Param(value = "profileId") String profileId);

    UserEntity selectUserByAdmin();

    UserEntity selectUserByContact(UserEntity user);

    ChatSendUserContactDto selectChatByIndex(@Param(value = "index") int index);
}