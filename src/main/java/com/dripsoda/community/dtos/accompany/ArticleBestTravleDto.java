package com.dripsoda.community.dtos.accompany;

import com.dripsoda.community.entities.accompany.ArticleEntity;

public class ArticleBestTravleDto extends ArticleEntity {

    private String regionText;

    private int articleRegionCount;

    public String getRegionText() {
        return regionText;
    }

    public ArticleBestTravleDto setRegionText(String regionText) {
        this.regionText = regionText;
        return this;
    }

    public int getArticleRegionCount() {
        return articleRegionCount;
    }

    public ArticleBestTravleDto setArticleRegionCount(int articleRegionCount) {
        this.articleRegionCount = articleRegionCount;
        return this;
    }
}
