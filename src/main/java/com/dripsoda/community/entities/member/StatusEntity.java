package com.dripsoda.community.entities.member;

import com.dripsoda.community.interfaces.IEntity;

import java.util.Objects;

public class StatusEntity implements IEntity<StatusEntity> {

    public static final String ATTRIBUTE_NAME = "memberStatus";
    public static final String ATTRIBUTE_NAME_PLURAL = "memberStatuses";

    public static StatusEntity build() {
        return new StatusEntity();
    }
    private String value;
    private String text;

    public StatusEntity() {
    }

    public StatusEntity(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public StatusEntity setValue(String value) {
        this.value = value;
        return this;
    }

    public String getText() {
        return text;
    }

    public StatusEntity setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusEntity that = (StatusEntity) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public StatusEntity clone() {
        StatusEntity statusEntity = new StatusEntity();
        this.value = statusEntity.value;
        this.text = statusEntity.text;
        return statusEntity;
    }

    @Override
    public void copyValuesOf(StatusEntity statusEntity) {
        statusEntity.value = this.value;
        statusEntity.text = this.text;
    }
}
