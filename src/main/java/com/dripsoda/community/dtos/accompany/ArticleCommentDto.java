package com.dripsoda.community.dtos.accompany;

import com.dripsoda.community.entities.accompany.CommentEntity;

public class ArticleCommentDto  extends CommentEntity {
    private String userName;

    private String userNickname;
    private String userProfileId;
    private byte[] userProfileData;

    public String getUserName() {
        return userName;
    }

    public ArticleCommentDto setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public ArticleCommentDto setUserNickname(String userNickname) {
        this.userNickname = userNickname;
        return this;
    }

    public String getUserProfileId() {
        return userProfileId;
    }

    public ArticleCommentDto setUserProfileId(String userProfileId) {
        this.userProfileId = userProfileId;
        return this;
    }

    public byte[] getUserProfileData() {
        return userProfileData;
    }

    public ArticleCommentDto setUserProfileData(byte[] userProfileData) {
        this.userProfileData = userProfileData;
        return this;
    }
}
