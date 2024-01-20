package com.dripsoda.community.entities.member;

import com.dripsoda.community.interfaces.IEntity;

import java.util.Date;
import java.util.Objects;

public class UserEntity implements IEntity<UserEntity> {
    public static final String ATTRIBUTE_NAME = "memberUser";
    public static final String ATTRIBUTE_NAME_PLURAL = "memberUsers";

    public static UserEntity build() {
        return new UserEntity();
    }

    private String email;
    private String password;
    private String name;
    private String nickname;
    private String contactCountryValue;
    private String contact;
    private Date policyTermsAt = new Date();
    private Date policyPrivacyAt = new Date();
    private Date policyMarketingAt;
    private String statusValue;
    private Date registeredAt = new Date();
    private boolean isAdmin = false;
    private String profileId;
    private byte[] profileData;


    public UserEntity(String email, String password, String name, String nickname, String contactCountryValue, String contact, Date policyTermsAt, Date policyPrivacyAt, Date policyMarketingAt, String statusValue, Date registeredAt, boolean isAdmin, String profileId, byte[] profileData) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.contactCountryValue = contactCountryValue;
        this.contact = contact;
        this.policyTermsAt = policyTermsAt;
        this.policyPrivacyAt = policyPrivacyAt;
        this.policyMarketingAt = policyMarketingAt;
        this.statusValue = statusValue;
        this.registeredAt = registeredAt;
        this.isAdmin = isAdmin;
        this.profileId = profileId;
        this.profileData = profileData;
    }

    public String getProfileId() {
        return profileId;
    }

    public UserEntity setProfileId(String profileId) {
        this.profileId = profileId;
        return this;
    }

    public byte[] getProfileData() {
        return profileData;
    }

    public UserEntity setProfileData(byte[] profileData) {
        this.profileData = profileData;
        return this;
    }

    public UserEntity() {
    }



    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public UserEntity setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getContactCountryValue() {
        return contactCountryValue;
    }

    public UserEntity setContactCountryValue(String contactCountryValue) {
        this.contactCountryValue = contactCountryValue;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public UserEntity setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public Date getPolicyTermsAt() {
        return policyTermsAt;
    }

    public UserEntity setPolicyTermsAt(Date policyTermsAt) {
        this.policyTermsAt = policyTermsAt;
        return this;
    }

    public Date getPolicyPrivacyAt() {
        return policyPrivacyAt;
    }

    public UserEntity setPolicyPrivacyAt(Date policyPrivacyAt) {
        this.policyPrivacyAt = policyPrivacyAt;
        return this;
    }

    public Date getPolicyMarketingAt() {
        return policyMarketingAt;
    }

    public UserEntity setPolicyMarketingAt(Date policyMarketingAt) {
        this.policyMarketingAt = policyMarketingAt;
        return this;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public UserEntity setStatusValue(String statusValue) {
        this.statusValue = statusValue;
        return this;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public UserEntity setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
        return this;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public UserEntity setAdmin(boolean admin) {
        isAdmin = admin;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public UserEntity clone() {
        UserEntity userEntity = new UserEntity();
        userEntity.email = this.email;
        userEntity.password = this.password;
        userEntity.name = this.name;
        userEntity.nickname = this.nickname;
        userEntity.contactCountryValue = this.contactCountryValue;
        userEntity.contact = this.contact;
        userEntity.policyTermsAt = this.policyTermsAt;
        userEntity.policyPrivacyAt = this.policyPrivacyAt;
        userEntity.policyMarketingAt = this.policyMarketingAt;
        userEntity.statusValue = this.statusValue;
        userEntity.registeredAt = this.registeredAt;
        userEntity.isAdmin = this.isAdmin;
        userEntity.profileId = this.profileId;
        userEntity.profileData = this.profileData;
        return userEntity;
    }

    @Override
    public void copyValuesOf(UserEntity userEntity) {
        this.email = userEntity.email;
        this.password = userEntity.password;
        this.name = userEntity.name;
        this.nickname =  userEntity.nickname;
        this.contactCountryValue =  userEntity.contactCountryValue;
        this.contact = userEntity.contact;
        this.policyTermsAt =  userEntity.policyTermsAt;
        this.policyPrivacyAt = userEntity.policyPrivacyAt;
        this.policyMarketingAt = userEntity.policyMarketingAt;
        this.statusValue = userEntity.statusValue;
        this.registeredAt = userEntity.registeredAt;
        this.isAdmin =  userEntity.isAdmin;
        this.profileId = userEntity.profileId;
        this.profileData = userEntity.profileData;
    }
}
