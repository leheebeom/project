package com.dripsoda.community.entities.member;

import com.dripsoda.community.interfaces.IEntity;

import java.util.Date;
import java.util.Objects;

public class FeedbackEntity implements IEntity<FeedbackEntity> {


    public static FeedbackEntity build() {
        return new FeedbackEntity();
    }
    private int index;
    private String userEmail;
    private Date createdAt;
    private boolean isFind;
    private boolean isTrip;
    private boolean isProduct;
    private boolean isManner;
    private boolean isConvenience;
    private boolean isNew;
    private boolean isUseful;

    private String content;

    public FeedbackEntity() {
    }

    public FeedbackEntity(int index, String userEmail, Date createdAt, boolean isFind, boolean isTrip, boolean isProduct, boolean isManner, boolean isConvenience, boolean isNew, boolean isUseful,String content) {
        this.index = index;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
        this.isFind = isFind;
        this.isTrip = isTrip;
        this.isProduct = isProduct;
        this.isManner = isManner;
        this.isConvenience = isConvenience;
        this.isNew = isNew;
        this.isUseful = isUseful;
        this.content = content;
    }

    public int getIndex() {
        return index;
    }

    public FeedbackEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public FeedbackEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public FeedbackEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public boolean isFind() {
        return isFind;
    }

    public FeedbackEntity setFind(boolean find) {
        isFind = find;
        return this;
    }

    public boolean isTrip() {
        return isTrip;
    }

    public FeedbackEntity setTrip(boolean trip) {
        isTrip = trip;
        return this;
    }

    public boolean isProduct() {
        return isProduct;
    }

    public FeedbackEntity setProduct(boolean product) {
        isProduct = product;
        return this;
    }

    public boolean isManner() {
        return isManner;
    }

    public FeedbackEntity setManner(boolean manner) {
        isManner = manner;
        return this;
    }

    public boolean isConvenience() {
        return isConvenience;
    }

    public FeedbackEntity setConvenience(boolean convenience) {
        isConvenience = convenience;
        return this;
    }

    public boolean isNew() {
        return isNew;
    }

    public FeedbackEntity setNew(boolean aNew) {
        isNew = aNew;
        return this;
    }

    public boolean isUseful() {
        return isUseful;
    }

    public FeedbackEntity setUseful(boolean useful) {
        isUseful = useful;
        return this;
    }

    public String getContent() {
        return content;
    }

    public FeedbackEntity setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackEntity that = (FeedbackEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public FeedbackEntity clone() {
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.index = this.index;
        feedbackEntity.createdAt = this.createdAt;
        feedbackEntity.isFind = this.isFind;
        feedbackEntity.isTrip = this.isTrip;
        feedbackEntity.isProduct = this.isProduct;
        feedbackEntity.isManner = this.isManner;
        feedbackEntity.isConvenience = this.isConvenience;
        feedbackEntity.isNew = this.isNew;
        feedbackEntity.isUseful = this.isUseful;
        feedbackEntity.content = this.content;
        return feedbackEntity;
    }

    @Override
    public void copyValuesOf(FeedbackEntity feedbackEntity) {
        this.index = feedbackEntity.index;
        this.createdAt = feedbackEntity.createdAt;
        this.isFind = feedbackEntity.isFind;
        this.isTrip = feedbackEntity.isTrip;
        this.isProduct = feedbackEntity.isProduct;
        this.isManner = feedbackEntity.isManner;
        this.isConvenience = feedbackEntity.isConvenience;
        this.isNew = feedbackEntity.isNew;
        this.isUseful = feedbackEntity.isUseful;
        this.content = feedbackEntity.content;
    }
}
