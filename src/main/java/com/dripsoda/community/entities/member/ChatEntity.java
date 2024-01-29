package com.dripsoda.community.entities.member;

import com.dripsoda.community.interfaces.IEntity;

import java.util.Date;
import java.util.Objects;

public class ChatEntity implements IEntity<ChatEntity> {
    public static final String ATTRIBUTE_NAME = "memberChat";
    public static final String ATTRIBUTE_NAME_PLURAL = "memberChats";

    public static ChatEntity build() {
        return new ChatEntity();
    }

    private int index;
    private int room;
    private String sendUserEmail;
    private String receiveUserEmail;
    private Date sendTime;
    private Date readTime;
    private String content;
    private int readChecked;

    public ChatEntity() {
    }

    public ChatEntity(int index, int room, String sendUserEmail, String receiveUserEmail, Date sendTime, Date readTime, String content, int readChecked) {
        this.index = index;
        this.room = room;
        this.sendUserEmail = sendUserEmail;
        this.receiveUserEmail = receiveUserEmail;
        this.sendTime = sendTime;
        this.readTime = readTime;
        this.content = content;
        this.readChecked = readChecked;
    }

    public int getIndex() {
        return index;
    }

    public ChatEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getRoom() {
        return room;
    }

    public ChatEntity setRoom(int room) {
        this.room = room;
        return this;
    }

    public String getSendUserEmail() {
        return sendUserEmail;
    }

    public ChatEntity setSendUserEmail(String sendUserEmail) {
        this.sendUserEmail = sendUserEmail;
        return this;
    }

    public String getReceiveUserEmail() {
        return receiveUserEmail;
    }

    public ChatEntity setReceiveUserEmail(String receiveUserEmail) {
        this.receiveUserEmail = receiveUserEmail;
        return this;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public ChatEntity setSendTime(Date sendTime) {
        this.sendTime = sendTime;
        return this;
    }

    public Date getReadTime() {
        return readTime;
    }

    public ChatEntity setReadTime(Date readTime) {
        this.readTime = readTime;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ChatEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public int getReadChecked() {
        return readChecked;
    }

    public ChatEntity setReadChecked(int readChecked) {
        this.readChecked = readChecked;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatEntity that = (ChatEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public ChatEntity clone() {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.index = this.index;
        chatEntity.room = this.room;
        chatEntity.sendUserEmail = this.sendUserEmail;
        chatEntity.receiveUserEmail = this.receiveUserEmail;
        chatEntity.sendTime = this.sendTime;
        chatEntity.readTime = this.readTime;
        chatEntity.content = this.content;
        chatEntity.readChecked = this.readChecked;
        return chatEntity;
    }

    @Override
    public void copyValuesOf(ChatEntity chatEntity) {
        this.index = chatEntity.getIndex();;
        this.room = chatEntity.getRoom();
        this.sendUserEmail = chatEntity.getSendUserEmail();
        this.receiveUserEmail = chatEntity.getReceiveUserEmail();
        this.sendTime = chatEntity.getSendTime();
        this.readTime = chatEntity.getReadTime();
        this.content = chatEntity.getContent();
        this.readChecked = chatEntity.readChecked;

    }
}
