package com.dripsoda.community.entities.accompany;

import java.util.Objects;

public class CountryEntity {


    public static final String ATTRIBUTE_NAME = "accompanyCountry";
    public static final String ATTRIBUTE_NAME_PLURAL = "accompanyCountries";

    public static CountryEntity build() {
        return new CountryEntity();
    }

    private String continentValue;
    private String value;
    private String text;

    public CountryEntity() {
    }

    public CountryEntity(String continentValue, String value, String text) {
        this.continentValue = continentValue;
        this.value = value;
        this.text = text;
    }

    public String getContinentValue() {
        return continentValue;
    }

    public CountryEntity setContinentValue(String continentValue) {
        this.continentValue = continentValue;
        return this;
    }

    public String getValue() {
        return value;
    }

    public CountryEntity setValue(String value) {
        this.value = value;
        return this;
    }

    public String getText() {
        return text;
    }

    public CountryEntity setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryEntity that = (CountryEntity) o;
        return Objects.equals(continentValue, that.continentValue) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(continentValue, value);
    }
}
