package com.dripsoda.community.entities.accompany;

import com.dripsoda.community.interfaces.IEntity;

public class CommentLikeEntity implements IEntity<CommentLikeEntity> {
    public static final String ATTRIBUTE_NAME = "accompanyCommentLike";
    public static final String ATTRIBUTE_NAME_PLURAL = "accompanyCommentLikes";
    public static CommentLikeEntity build() {
        return new CommentLikeEntity();
    }

    private int index;
    private int articleIndex;
    private String userEmail;


    private int commentIndex;
    private boolean isChecked;

    public CommentLikeEntity() {
    }

    public CommentLikeEntity(int index, int articleIndex, String userEmail, int commentIndex, boolean isChecked) {
        this.index = index;
        this.articleIndex = articleIndex;
        this.userEmail = userEmail;
        this.commentIndex = commentIndex;
        this.isChecked = isChecked;
    }

    public int getIndex() {
        return index;
    }

    public CommentLikeEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getArticleIndex() {
        return articleIndex;
    }

    public CommentLikeEntity setArticleIndex(int articleIndex) {
        this.articleIndex = articleIndex;
        return this;
    }
    public String getUserEmail() {
        return userEmail;
    }

    public CommentLikeEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }



    public int getCommentIndex() {
        return commentIndex;
    }

    public CommentLikeEntity setCommentIndex(int commentIndex) {
        this.commentIndex = commentIndex;
        return this;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public CommentLikeEntity setChecked(boolean checked) {
        isChecked = checked;
        return this;
    }

    @Override
    public CommentLikeEntity clone() {
        CommentLikeEntity commentLikeEntity = new CommentLikeEntity();
        commentLikeEntity.index = this.index;
        commentLikeEntity.articleIndex = this.articleIndex;
        commentLikeEntity.userEmail = this.userEmail;
        commentLikeEntity.commentIndex = this.commentIndex;
        commentLikeEntity.isChecked = this.isChecked;
        return commentLikeEntity;
    }

    @Override
    public void copyValuesOf(CommentLikeEntity commentLikeEntity) {
        this.index = commentLikeEntity.index;
        this.articleIndex = commentLikeEntity.articleIndex;
        this.userEmail = commentLikeEntity.userEmail;;
        this.commentIndex = commentLikeEntity.commentIndex;
        this.isChecked = commentLikeEntity.isChecked;
    }
}
