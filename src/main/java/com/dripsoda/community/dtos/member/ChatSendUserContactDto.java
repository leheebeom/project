package com.dripsoda.community.dtos.member;

import com.dripsoda.community.entities.member.ChatEntity;

public class ChatSendUserContactDto extends ChatEntity {
    private String sendUserContact;

    public String getSendUserContact() {
        return sendUserContact;
    }

    public ChatSendUserContactDto setSendUserContact(String sendUserContact) {
        this.sendUserContact = sendUserContact;
        return this;
    }
}
