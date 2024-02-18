package com.dripsoda.community.entities.qna;

import com.dripsoda.community.interfaces.IEntity;

import java.util.Objects;

public class CategoryEntity implements IEntity<CategoryEntity> {
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

    @Override
    public CategoryEntity clone() {
        CategoryEntity categoryEntity = new CategoryEntity();
        this.id = categoryEntity.id;
        this.text = categoryEntity.text;
        return categoryEntity;
    }

    @Override
    public void copyValuesOf(CategoryEntity categoryEntity) {
        categoryEntity.id = this.id;
        categoryEntity.text = this.text;
    }
}
