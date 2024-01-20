package com.dripsoda.community.mappers;

import com.dripsoda.community.entities.member.*;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IMemberMapper {
    int insertContactAuth(ContactAuthEntity contactAuth);

    int insertEmailAuth(EmailAuthEntity emailAuth);

    int insertUser(UserEntity user);

//    String updateUserProfileImage(UserEntity newUser);

    ContactAuthEntity selectContactAuthByContactCodeSalt(ContactAuthEntity contactAuth);

    ContactCountryEntity[] selectContactCountries();

    EmailAuthEntity selectEmailAuthByIndex(EmailAuthEntity emailAuth);

    UserEntity selectUserByEmail(UserEntity user);

    int insertFeedback(FeedbackEntity feedback);
    UserEntity selectUserByPassword(UserEntity user);

    UserEntity selectUserByEmailPassword(UserEntity user);

    UserEntity selectUserByNameContact(UserEntity user);

    UserEntity selectUserByContact(UserEntity user);

    int updateContactAuth(ContactAuthEntity contactAuth);

    int updateEmailAuth(EmailAuthEntity emailAuth);

    int updateUser(UserEntity user);

    int deleteUser(UserEntity user);

    UserEntity selectProfileImage(@Param(value = "profileId")String profileId);

    List<UserEntity> selectUsersForHome();
}