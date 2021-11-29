package com.thikthak.app.domain.service;

import com.thikthak.app.acl.auth.domain.User;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "service_orders")
public class ServiceOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    Long id;

    // Order
    String orderCode;
    Date orderPlaceTime;

    // Client
    @ManyToOne
    User clientUser;
    String clientUserName;
    String clientGeoLocation;
    Double clientLatitude;
    Double clientLongitude;

    // Technician
    @ManyToOne
    User technicianUser;
    String technicianUserName;
    String technicianGeoLocation;
    Double techLatitude;
    Double techLongitude;

    // Service request item
//    ServiceItems serviceItems
    @Column(name = "SERVICE_ITEMS_CODE")
    String serviceItemCode;
    @Column(name = "SERVICE_ITEMS_NAME")
    String serviceItemName;
    String serviceDetailsDesc;
    String issueImagePath;

    Boolean warrantyProduct;
    Boolean servicePartsRequired;
    Boolean servicePartsRequiredAcknlg;
    Boolean isAgreed;
//    @Column(name="SERVICE_PARTS_PROVIDED", columnDefinition="NUMBER(1,0) default 0")
    @Column(name="SERVICE_PARTS_PROVIDED")
    Boolean servicePartsProvided;
    String refInvoiceNumber;


    @Column(name="IS_SCHEDULE_ORDER")
    Boolean isScheduleOrder;
    Date scheduleDate;
    public Boolean getScheduleOrder() {
        return isScheduleOrder;
    }
    public void setScheduleOrder(Boolean scheduleOrder) {
        isScheduleOrder = scheduleOrder;
    }
    public Boolean getIsScheduleOrder() {
        return isScheduleOrder;
    }
    public void setIsScheduleOrder(Boolean isScheduleOrder) {
        this.isScheduleOrder = isScheduleOrder;
    }
    public Date getScheduleDate() {
        return scheduleDate;
    }
    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    String brandName;
    String productSerial;
    Date serviceStartTime;
    Date serviceEndTime;
    Double serviceDuration;

    Double billAmount;
    Double sdAmount;
    Double vatAmount;
    Double cashRcvAmount;
    Double cardRcvAmount;
    Double totalRcvAmount;


    Double serviceCost; // serviceCost = comChargeAmount + techAmount
    Double comChargePct;
    Double comChargeAmount;
    Double techAmount;

    Date bookTime;
    String deliveryAddress;


    Integer status = 0; // Pending-0, Accept-1, Complete-2 Reject-3

    Double orderPrice;
    // Payment
    Double paymentAmount;

    Boolean isRated = false;
    Double rating;

    // System log fields
    @Column(name = "CREATION_DATETIME")
    Date creationDateTime;
    @Column(name = "CREATION_USER")
    String creationUser;
    @Column(name = "LAST_UPDATE_DATETIME")
    Date lastUpdateDateTime;
    @Column(name = "LAST_UPDATE_USER")
    String lastUpdateUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getOrderPlaceTime() {
        return orderPlaceTime;
    }

    public void setOrderPlaceTime(Date orderPlaceTime) {
        this.orderPlaceTime = orderPlaceTime;
    }

    public User getClientUser() {
        return clientUser;
    }

    public void setClientUser(User clientUser) {
        this.clientUser = clientUser;
    }

    public String getClientUserName() {
        return clientUserName;
    }

    public void setClientUserName(String clientUserName) {
        this.clientUserName = clientUserName;
    }

    public String getClientGeoLocation() {
        return clientGeoLocation;
    }

    public void setClientGeoLocation(String clientGeoLocation) {
        this.clientGeoLocation = clientGeoLocation;
    }

    public Double getClientLatitude() {
        return clientLatitude;
    }

    public void setClientLatitude(Double clientLatitude) {
        this.clientLatitude = clientLatitude;
    }

    public Double getClientLongitude() {
        return clientLongitude;
    }

    public void setClientLongitude(Double clientLongitude) {
        this.clientLongitude = clientLongitude;
    }

    public User getTechnicianUser() {
        return technicianUser;
    }

    public void setTechnicianUser(User technicianUser) {
        this.technicianUser = technicianUser;
    }

    public String getTechnicianUserName() {
        return technicianUserName;
    }

    public void setTechnicianUserName(String technicianUserName) {
        this.technicianUserName = technicianUserName;
    }

    public String getTechnicianGeoLocation() {
        return technicianGeoLocation;
    }

    public void setTechnicianGeoLocation(String technicianGeoLocation) {
        this.technicianGeoLocation = technicianGeoLocation;
    }

    public Double getTechLatitude() {
        return techLatitude;
    }

    public void setTechLatitude(Double techLatitude) {
        this.techLatitude = techLatitude;
    }

    public Double getTechLongitude() {
        return techLongitude;
    }

    public void setTechLongitude(Double techLongitude) {
        this.techLongitude = techLongitude;
    }



    public String getServiceItemCode() {
        return serviceItemCode;
    }

    public void setServiceItemCode(String serviceItemCode) {
        this.serviceItemCode = serviceItemCode;
    }

    public String getServiceItemName() {
        return serviceItemName;
    }

    public void setServiceItemName(String serviceItemName) {
        this.serviceItemName = serviceItemName;
    }

    public String getServiceDetailsDesc() {
        return serviceDetailsDesc;
    }

    public void setServiceDetailsDesc(String serviceDetailsDesc) {
        this.serviceDetailsDesc = serviceDetailsDesc;
    }

    public String getIssueImagePath() {
        return issueImagePath;
    }

    public void setIssueImagePath(String issueImagePath) {
        this.issueImagePath = issueImagePath;
    }


    public Boolean getWarrantyProduct() {
        return warrantyProduct;
    }

    public void setWarrantyProduct(Boolean warrantyProduct) {
        this.warrantyProduct = warrantyProduct;
    }

    public Boolean getServicePartsRequired() {
        return servicePartsRequired;
    }

    public void setServicePartsRequired(Boolean servicePartsRequired) {
        this.servicePartsRequired = servicePartsRequired;
    }

    public Boolean getServicePartsRequiredAcknlg() {
        return servicePartsRequiredAcknlg;
    }

    public void setServicePartsRequiredAcknlg(Boolean servicePartsRequiredAcknlg) {
        this.servicePartsRequiredAcknlg = servicePartsRequiredAcknlg;
    }

    public Boolean getIsAgreed() {
        return isAgreed;
    }

    public void setIsAgreed(Boolean isAgreed) {
        this.isAgreed = isAgreed;
    }

    public Boolean getServicePartsProvided() {
        return servicePartsProvided;
    }

    public void setServicePartsProvided(Boolean servicePartsProvided) {
        this.servicePartsProvided = servicePartsProvided;
    }



    public String getRefInvoiceNumber() {
        return refInvoiceNumber;
    }

    public void setRefInvoiceNumber(String refInvoiceNumber) {
        this.refInvoiceNumber = refInvoiceNumber;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductSerial() {
        return productSerial;
    }

    public void setProductSerial(String productSerial) {
        this.productSerial = productSerial;
    }

    public Date getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(Date serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public Date getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(Date serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    public Double getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(Double serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    public Double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    public Double getSdAmount() {
        return sdAmount;
    }

    public void setSdAmount(Double sdAmount) {
        this.sdAmount = sdAmount;
    }

    public Double getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(Double vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Double getCashRcvAmount() {
        return cashRcvAmount;
    }

    public void setCashRcvAmount(Double cashRcvAmount) {
        this.cashRcvAmount = cashRcvAmount;
    }

    public Double getCardRcvAmount() {
        return cardRcvAmount;
    }

    public void setCardRcvAmount(Double cardRcvAmount) {
        this.cardRcvAmount = cardRcvAmount;
    }

    public Double getTotalRcvAmount() {
        return totalRcvAmount;
    }

    public void setTotalRcvAmount(Double totalRcvAmount) {
        this.totalRcvAmount = totalRcvAmount;
    }

    public Date getBookTime() {
        return bookTime;
    }

    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Boolean getRated() {
        return isRated;
    }

    public void setRated(Boolean rated) {
        isRated = rated;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }



    public Double getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(Double serviceCost) {
        this.serviceCost = serviceCost;
    }

    public Double getComChargePct() {
        return comChargePct;
    }

    public void setComChargePct(Double comChargePct) {
        this.comChargePct = comChargePct;
    }

    public Double getComChargeAmount() {
        return comChargeAmount;
    }

    public void setComChargeAmount(Double comChargeAmount) {
        this.comChargeAmount = comChargeAmount;
    }

    public Double getTechAmount() {
        return techAmount;
    }

    public void setTechAmount(Double techAmount) {
        this.techAmount = techAmount;
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
