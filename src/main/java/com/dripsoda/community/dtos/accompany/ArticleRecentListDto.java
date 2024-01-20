package com.dripsoda.community.dtos.accompany;

import com.dripsoda.community.entities.accompany.ArticleEntity;

public class ArticleRecentListDto extends ArticleEntity {
    private String userName;

    private String userNickname;


    private String regionText;

    public String getUserNickname() {
        return userNickname;
    }

    public ArticleRecentListDto setUserNickname(String userNickname) {
        this.userNickname = userNickname;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public ArticleRecentListDto setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getRegionText() {
        return regionText;
    }

    public ArticleRecentListDto setRegionText(String regionText) {
        this.regionText = regionText;
        return this;
    }
}
