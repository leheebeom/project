package com.dripsoda.community.vos.member;

import com.dripsoda.community.entities.member.UserEntity;
import com.dripsoda.community.enums.member.UserLoginResult;
import com.dripsoda.community.interfaces.IResult;

public class LoginVo extends UserEntity {
    private boolean autosign;



    public boolean isAutosign() {
        return autosign;
    }

    public LoginVo setAutosign(boolean autosign) {
        this.autosign = autosign;
        return this;
    }


}
