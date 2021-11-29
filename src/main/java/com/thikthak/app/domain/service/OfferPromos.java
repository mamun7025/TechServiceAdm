package com.thikthak.app.domain.service;

import com.thikthak.app.acl.auth.domain.User;
import com.thikthak.app.domain.base.Organization;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="OFFER_PROMOS")
public class OfferPromos {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="ORGANIZATION_ID", referencedColumnName = "ID", nullable = true)
    Organization organization;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = true)
    User promoUser;

    String title;
    String type;
    Double discountAmount;
    String promoCode;
    Boolean isPromoCode;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date expiryDate;
    String conditions;
    Integer leftPromo;
    Boolean isExpired;

    // System log fields
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date creationDateTime;
    String creationUser;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date lastUpdateDateTime;
    String lastUpdateUser;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public User getPromoUser() {
        return promoUser;
    }

    public void setPromoUser(User promoUser) {
        this.promoUser = promoUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Boolean getIsPromoCode() {
        return isPromoCode;
    }

    public void setIsPromoCode(Boolean promoCode) {
        isPromoCode = promoCode;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public Integer getLeftPromo() {
        return leftPromo;
    }

    public void setLeftPromo(Integer leftPromo) {
        this.leftPromo = leftPromo;
    }

    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean expired) {
        isExpired = expired;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getCreationUser() {
        return creationUser;
    }

    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }



}
