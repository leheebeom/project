package com.dripsoda.community.entities.accompany;

import com.dripsoda.community.interfaces.IEntity;

import java.util.Date;
import java.util.Objects;

public class ArticleEntity implements IEntity<ArticleEntity> {

    public static final String ATTRIBUTE_NAME = "accompanyArticle";
    public static final String ATTRIBUTE_NAME_PLURAL = "accompanyArticles";

    public static ArticleEntity build() {
        return new ArticleEntity();
    }

    private int index;
    private String userEmail;
    private Date createdAt;
    private String continentValue;
    private String countryValue;
    private String regionValue;
    private int capacity;
    private Date dateFrom;
    private Date dateTo;
    private byte[] coverImage;
    private String coverImageMime;
    private String title;
    private String content;

    private int viewCount;

    public ArticleEntity() {
    }

    public ArticleEntity(int index, String userEmail, Date createdAt, String continentValue, String countryValue, String regionValue, int capacity, Date dateFrom, Date dateTo, byte[] coverImage, String coverImageMime, String title, String content, int viewCount) {
        this.index = index;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
        this.continentValue = continentValue;
        this.countryValue = countryValue;
        this.regionValue = regionValue;
        this.capacity = capacity;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.coverImage = coverImage;
        this.coverImageMime = coverImageMime;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
    }

    public int getIndex() {
        return index;
    }

    public ArticleEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public ArticleEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public ArticleEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getContinentValue() {
        return continentValue;
    }

    public ArticleEntity setContinentValue(String continentValue) {
        this.continentValue = continentValue;
        return this;
    }

    public String getCountryValue() {
        return countryValue;
    }

    public ArticleEntity setCountryValue(String countryValue) {
        this.countryValue = countryValue;
        return this;
    }

    public String getRegionValue() {
        return regionValue;
    }

    public ArticleEntity setRegionValue(String regionValue) {
        this.regionValue = regionValue;
        return this;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArticleEntity setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public ArticleEntity setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public ArticleEntity setDateTo(Date dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public ArticleEntity setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
        return this;
    }

    public String getCoverImageMime() {
        return coverImageMime;
    }

    public ArticleEntity setCoverImageMime(String coverImageMime) {
        this.coverImageMime = coverImageMime;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ArticleEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ArticleEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleEntity that = (ArticleEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public ArticleEntity clone() {
        ArticleEntity articleEntity = new ArticleEntity();
        this.index = articleEntity.index;
        this.userEmail = articleEntity.userEmail;
        this.createdAt = articleEntity.createdAt;
        this.continentValue = articleEntity.continentValue;
        this.countryValue = articleEntity.countryValue;
        this.regionValue = articleEntity.regionValue;
        this.capacity = articleEntity.capacity;
        this.dateFrom = articleEntity.dateFrom;
        this.dateTo = articleEntity.dateTo;
        this.coverImage = articleEntity.coverImage;
        this.coverImageMime = articleEntity.coverImageMime;
        this.title = articleEntity.title;
        this.content = articleEntity.content;
        this.viewCount = articleEntity.viewCount;
        return articleEntity;
    }

    @Override
    public void copyValuesOf(ArticleEntity articleEntity) {
        articleEntity.index = this.index;
        articleEntity.userEmail = this.userEmail;
        articleEntity.createdAt = this.createdAt;
        articleEntity.continentValue = this.continentValue;
        articleEntity.countryValue = this.countryValue;
        articleEntity.regionValue = this.regionValue;
        articleEntity.capacity = this.capacity;
        articleEntity.dateFrom = this.dateFrom;
        articleEntity.dateTo = this.dateTo;
        articleEntity.coverImage = this.coverImage;
        articleEntity.coverImageMime = this.coverImageMime;
        articleEntity.title = this.title;
        articleEntity.content = this.content;
        articleEntity.viewCount = this.viewCount;
    }
}
