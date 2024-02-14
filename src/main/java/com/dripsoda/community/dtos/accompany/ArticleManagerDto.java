package com.dripsoda.community.dtos.accompany;

import com.dripsoda.community.entities.accompany.ArticleEntity;

public class ArticleManagerDto extends ArticleEntity {
    private String userName;

    private String userNickname;


    private String regionText;

    public String getUserName() {
        return userName;
    }

    public ArticleManagerDto setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public ArticleManagerDto setUserNickname(String userNickname) {
        this.userNickname = userNickname;
        return this;
    }

    public String getRegionText() {
        return regionText;
    }

    public ArticleManagerDto setRegionText(String regionText) {
        this.regionText = regionText;
        return this;
    }
}
