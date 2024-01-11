package com.dripsoda.community.dtos.accompany;

import com.dripsoda.community.entities.accompany.ArticleEntity;

//유저 이름 때문에 dto 사용
public class ArticleSearchDto extends ArticleEntity {
    private String userName;

    private String userNickname;

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserName() {
        return userName;
    }

    public ArticleSearchDto setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
