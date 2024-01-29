package com.dripsoda.community.entities.qna;

import java.util.Date;
import java.util.Objects;

public class QnaArticleEntity {
    public static final String ATTRIBUTE_NAME = "qnaArticle";
    public static final String ATTRIBUTE_NAME_PLURAL = "qnaArticles";

    public static QnaArticleEntity build() {
        return new QnaArticleEntity();
    }

    private int index;

    private int categoryId;



    private String userEmail;
    private Date createdAt;
    private String title;
    private String content;

    private int viewCount;

    public QnaArticleEntity() {
    }

    public QnaArticleEntity(int index,int categoryId ,String userEmail, Date createdAt, String title, String content, int viewCount) {
        this.index = index;
        this.categoryId = categoryId;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
    }

    public int getIndex() {
        return index;
    }

    public QnaArticleEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public QnaArticleEntity setCategoryId(int categoryId) {
        this.categoryId = categoryId;
        return this;
    }
    public String getUserEmail() {
        return userEmail;
    }

    public QnaArticleEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public QnaArticleEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public QnaArticleEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public QnaArticleEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public int getViewCount() {
        return viewCount;
    }

    public QnaArticleEntity setViewCount(int viewCount) {
        this.viewCount = viewCount;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QnaArticleEntity that = (QnaArticleEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}

