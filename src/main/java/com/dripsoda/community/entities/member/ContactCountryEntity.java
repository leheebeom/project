package com.dripsoda.community.entities.member;

import com.dripsoda.community.interfaces.IEntity;

import java.util.Objects;

public class ContactCountryEntity implements IEntity<ContactCountryEntity> {

    public static final String ATTRIBUTE_NAME = "memberContactCountry";
    public static final String ATTRIBUTE_NAME_PLURAL = "memberContactCountries";

    public static ContactCountryEntity build() {
        return new ContactCountryEntity();
    }

    private String value;
    private String text;

    public ContactCountryEntity() {
    }

    public ContactCountryEntity(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public ContactCountryEntity setValue(String value) {
        this.value = value;
        return this;
    }

    public String getText() {
        return text;
    }

    public ContactCountryEntity setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactCountryEntity that = (ContactCountryEntity) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public ContactCountryEntity clone() {
        ContactCountryEntity contactCountryEntity = new ContactCountryEntity();
        contactCountryEntity.value = this.value;
        contactCountryEntity.text = this.text;
        return contactCountryEntity;
    }

    @Override
    public void copyValuesOf(ContactCountryEntity contactCountryEntity) {
        this.value = contactCountryEntity.value;
        this.text = contactCountryEntity.text;
    }
}

