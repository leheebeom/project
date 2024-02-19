package com.dripsoda.community.dtos.accompany;

import com.dripsoda.community.entities.accompany.ArticleEntity;

public class ArticleRegionDto extends ArticleEntity {
    private String regionText;

    public String getRegionText() {
        return regionText;
    }

    public void setRegionText(String regionText) {
        this.regionText = regionText;
    }
}
