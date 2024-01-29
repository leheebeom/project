package com.dripsoda.community.entities.qna;

import java.util.Objects;

public class CategoryEntity {
    public static final String ATTRIBUTE_NAME = "qnaCategory";
    public static final String ATTRIBUTE_NAME_PLURAL = "qnaCategories";

    public static CategoryEntity build(){
        return new CategoryEntity();
    }
    private int id;
    private String text;

    public CategoryEntity() {
    }

    public CategoryEntity(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public CategoryEntity setId(int id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public CategoryEntity setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryEntity that = (CategoryEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
