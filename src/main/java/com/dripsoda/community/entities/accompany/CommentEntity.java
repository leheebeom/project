package com.dripsoda.community.entities.accompany;

import com.dripsoda.community.interfaces.IEntity;

import java.util.Date;
import java.util.Objects;

public class CommentEntity implements IEntity<CommentEntity> {
    public static final String ATTRIBUTE_NAME = "accompanyComment";
    public static final String ATTRIBUTE_NAME_PLURAL = "accompanyComments";

    public static CommentEntity build() {
        return new CommentEntity();
    }

    private int index;
    private int articleIndex;

    private Integer commentParentIndex;
    private String userEmail;
    private Date createdAt;
    private String content;

    private Date modifiedAt;
    private Boolean isDeleted;

    //댓글 존재여부 대댓글을 위해
    private Boolean isComment;
    private Integer depth;
    private int likes;

    private long orderNumber;

    public Boolean getComment() {
        return isComment;
    }

    public CommentEntity setComment(Boolean comment) {
        isComment = comment;
        return this;
    }

    public Integer getDepth() {
        return depth;
    }

    public CommentEntity setDepth(Integer depth) {
        this.depth = depth;
        return this;
    }

    public int getLikes() {
        return likes;
    }

    public CommentEntity setLikes(int likes) {
        this.likes = likes;
        return this;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public CommentEntity setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }


    public CommentEntity() {
    }

    public CommentEntity(int index, int articleIndex, Integer commentParentIndex, String userEmail, Date createdAt, String content, Date modifiedAt, Boolean isDeleted, Boolean isComment, Integer depth, int likes, long orderNumber) {
        this.index = index;
        this.articleIndex = articleIndex;
        this.commentParentIndex = commentParentIndex;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
        this.content = content;
        this.modifiedAt = modifiedAt;
        this.isDeleted = isDeleted;
        this.isComment = isComment;
        this.depth = depth;
        this.likes = likes;
        this.orderNumber = orderNumber;
    }

    public int getIndex() {
        return index;
    }

    public CommentEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getArticleIndex() {
        return articleIndex;
    }

    public CommentEntity setArticleIndex(int articleIndex) {
        this.articleIndex = articleIndex;
        return this;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public CommentEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public CommentEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getContent() {
        return content;
    }

    public CommentEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public Integer getCommentParentIndex() {
        return commentParentIndex;
    }

    public CommentEntity setCommentParentIndex(Integer commentParentIndex) {
        this.commentParentIndex = commentParentIndex;
        return this;
    }


    public Date getModifiedAt() {
        return modifiedAt;
    }

    public CommentEntity setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public CommentEntity setDeleted(Boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }


    @Override
    public CommentEntity clone() {
        CommentEntity commentEntity = new CommentEntity();
        this.index = commentEntity.index;
        this.articleIndex = commentEntity.articleIndex;
        this.commentParentIndex = commentEntity.commentParentIndex;
        this.userEmail = commentEntity.userEmail;
        this.createdAt = commentEntity.createdAt;
        this.content = commentEntity.content;
        this.modifiedAt = commentEntity.modifiedAt;
        this.isDeleted = commentEntity.isDeleted;
        this.isComment = commentEntity.isComment;
        this.depth = commentEntity.depth;
        this.likes = commentEntity.likes;
        this.orderNumber = commentEntity.orderNumber;
        return commentEntity;
    }

    @Override
    public void copyValuesOf(CommentEntity commentEntity) {
        commentEntity.index = this.index;
        commentEntity.articleIndex = this.articleIndex;
        commentEntity.commentParentIndex = this.commentParentIndex;
        commentEntity.userEmail = this.userEmail;
        commentEntity.createdAt = this.createdAt;
        commentEntity.content = this.content;
        commentEntity.modifiedAt = this.modifiedAt;
        commentEntity.isDeleted = this.isDeleted;
        commentEntity.isComment = this.isComment;
        commentEntity.depth = this.depth;
        commentEntity.likes = this.likes;
        commentEntity.orderNumber = this.orderNumber;
    }
}
