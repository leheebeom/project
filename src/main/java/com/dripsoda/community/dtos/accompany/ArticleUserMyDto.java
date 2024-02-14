package com.dripsoda.community.dtos.accompany;

import com.dripsoda.community.entities.accompany.ArticleEntity;

public class ArticleUserMyDto extends ArticleEntity {
    private String userName;

    private String userNickname;
    private String userProfileId;
    private byte[] userProfileData;
    private String regionText;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(String userProfileId) {
        this.userProfileId = userProfileId;
    }

    public byte[] getUserProfileData() {
        return userProfileData;
    }

    public void setUserProfileData(byte[] userProfileData) {
        this.userProfileData = userProfileData;
    }

    public String getRegionText() {
        return regionText;
    }

    public void setRegionText(String regionText) {
        this.regionText = regionText;
    }
}
