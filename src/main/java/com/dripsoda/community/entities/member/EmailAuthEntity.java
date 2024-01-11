package com.dripsoda.community.entities.member;

import java.util.Date;
import java.util.Objects;

public class EmailAuthEntity {
    public static final String ATTRIBUTE_NAME = "memberEmailAuth";
    public static final String ATTRIBUTE_NAME_PLURAL = "memberEmailAuths";

    public static EmailAuthEntity build() {
        return new EmailAuthEntity();
    }

    private int index = -1;
    private String email;
    private String code;
    private Date createdAt = new Date();
    private Date expiresAt;
    private boolean isExpired = false;

    public EmailAuthEntity() {
    }

    public EmailAuthEntity(int index, String email, String code, Date createdAt, Date expiresAt, boolean isExpired) {
        this.index = index;
        this.email = email;
        this.code = code;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.isExpired = isExpired;
    }

    public int getIndex() {
        return this.index;
    }

    public EmailAuthEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public EmailAuthEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public EmailAuthEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public EmailAuthEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getExpiresAt() {
        return this.expiresAt;
    }

    public EmailAuthEntity setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public boolean isExpired() {
        return this.isExpired;
    }

    public EmailAuthEntity setExpired(boolean isExpired) {
        this.isExpired = isExpired;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailAuthEntity that = (EmailAuthEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}