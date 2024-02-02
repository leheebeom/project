package com.dripsoda.community.dtos.accompany;

import com.dripsoda.community.entities.accompany.ArticleEntity;

public class ArticleKeywordDto extends ArticleEntity {
    private String regionText;

    public String getRegionText() {
        return regionText;
    }
    private String userName;

    public String getUserName() {
        return userName;
    }

    public ArticleKeywordDto setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public ArticleKeywordDto setUserNickname(String userNickname) {
        this.userNickname = userNickname;
        return this;
    }

    private String userNickname;

    public ArticleKeywordDto setRegionText(String regionText) {
        this.regionText = regionText;
        return this;
    }
}
